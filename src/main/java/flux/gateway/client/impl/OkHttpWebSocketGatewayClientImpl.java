package flux.gateway.client.impl;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import flux.api.ApiEnvironment;
import flux.api.FluxClient;
import flux.api.event.Event;
import flux.api.event.guild.GuildCreateEvent;
import flux.api.event.guild.member.GuildMemberAddEvent;
import flux.api.event.guild.member.GuildMemberRemoveEvent;
import flux.api.event.guild.member.GuildMemberUpdateEvent;
import flux.api.event.interaction.InteractionCreateEvent;
import flux.api.event.message.MessageDeleteEvent;
import flux.api.event.message.MessageUpdateEvent;
import flux.api.event.voice.VoiceServerUpdateEvent;
import flux.api.event.voice.VoiceStateUpdateEvent;
import flux.api.exception.FluxException;
import flux.api.gateway.EventDispatcher;
import flux.api.gateway.GatewayIntent;
import flux.gateway.client.GatewayClient;
import flux.json.util.JsonEngine;
import flux.model.gateway.*;
import flux.model.guild.GuildImpl;
import flux.model.interaction.SlashCommandInteractionImpl;
import flux.model.member.MemberImpl;
import flux.model.message.MessageImpl;
import flux.model.role.RoleImpl;
import flux.model.voice.VoiceStateImpl;
import okhttp3.*;
import okio.ByteString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class OkHttpWebSocketGatewayClientImpl implements GatewayClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(OkHttpWebSocketGatewayClientImpl.class);
    private static final String ENCODING = "json";

    private final OkHttpClient httpClient;
    private final JsonEngine jsonEngine;
    private final EventDispatcher eventDispatcher;

    private final AtomicInteger sequence = new AtomicInteger(-1);
    private final AtomicBoolean receivedHeartbeatAck = new AtomicBoolean(true);
    private final AtomicReference<GatewayState> state = new AtomicReference<>(GatewayState.DISCONNECTED);

    private WebSocket webSocket;
    private String botToken;
    private int intentsBitmask;
    private ScheduledExecutorService heartbeatExecutor;
    private ScheduledFuture<?> heartbeatTask;

    private String sessionId;
    private String resumeGatewayUrl;

    private CompletableFuture<Void> connectionFuture;

    public OkHttpWebSocketGatewayClientImpl(OkHttpClient httpClient, JsonEngine jsonEngine,
            EventDispatcher eventDispatcher) {
        this.httpClient = Objects.requireNonNull(httpClient, "OkHttpClient cannot be null");
        this.jsonEngine = Objects.requireNonNull(jsonEngine, "JsonEngine cannot be null");
        this.eventDispatcher = Objects.requireNonNull(eventDispatcher, "EventDispatcher cannot be null");
        this.resumeGatewayUrl = ApiEnvironment.getGatewayUrl();
    }

    @Override
    public void setGatewayUrl(String gatewayUrl) {
        this.resumeGatewayUrl = gatewayUrl;
    }

    @Override
    public void setBotToken(String botToken) {
        this.botToken = botToken;
    }

    @Override
    public void setIntents(Collection<GatewayIntent> intents) {
        this.intentsBitmask = GatewayIntent.calculateBitmask(intents);
    }

    @Override
    public synchronized CompletableFuture<Void> connect() {
        if (state.get() != GatewayState.DISCONNECTED) {
            LOGGER.warn("Gateway connect() called while not in DISCONNECTED state (current: {})", state.get());
            return connectionFuture != null ? connectionFuture
                    : CompletableFuture.failedFuture(new IllegalStateException("Already connected or connecting."));
        }
        if (botToken == null)
            return CompletableFuture.failedFuture(new IllegalStateException("Bot token not set."));

        state.set(GatewayState.CONNECTING);
        if (connectionFuture == null || connectionFuture.isDone()) {
            connectionFuture = new CompletableFuture<>();
        }

        LOGGER.debug("connecting to Fluxer gateway");
        establishWebSocketConnection();
        return connectionFuture;
    }

    private void establishWebSocketConnection() {
        if (state.get() != GatewayState.CONNECTING && state.get() != GatewayState.RECONNECTING) {
            LOGGER.warn("Establish WebSocket called in invalid state: {}", state.get());
            return;
        }

        String url = (sessionId != null && resumeGatewayUrl != null) ? resumeGatewayUrl : ApiEnvironment.getGatewayUrl();
        String fullGatewayUrl = url + "/?v=" + ApiEnvironment.getGatewayVersion() + "&encoding=" + ENCODING;

        LOGGER.debug("websocket {}", fullGatewayUrl);
        Request request = new Request.Builder().url(fullGatewayUrl).build();
        webSocket = httpClient.newWebSocket(request, new FluxWebSocketListener());
    }

    private void attemptReconnect(boolean isResumable) {
        if (!state.compareAndSet(GatewayState.CONNECTED, GatewayState.RECONNECTING) &&
                !state.compareAndSet(GatewayState.DISCONNECTED, GatewayState.RECONNECTING) &&
                !state.compareAndSet(GatewayState.RESUMING, GatewayState.RECONNECTING)) {
            LOGGER.warn("Could not start reconnect, client is already in state {}.", state.get());
            return;
        }

        LOGGER.info("Attempting to reconnect... Resumable: {}", isResumable);
        stopHeartbeat();
        if (webSocket != null) {
            webSocket.close(4000, "Reconnecting");
            webSocket = null;
        }

        if (!isResumable) {
            LOGGER.warn("Session is not resumable. A new session will be started.");
            this.sessionId = null;
            this.sequence.set(-1);
            this.resumeGatewayUrl = ApiEnvironment.getGatewayUrl();
        }

        try {
            TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(1, 6));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        state.set(GatewayState.CONNECTING);
        establishWebSocketConnection();
    }

    @Override
    public synchronized void disconnect() {
        if (state.get() == GatewayState.DISCONNECTED || state.get() == GatewayState.SHUTTING_DOWN)
            return;
        LOGGER.info("Disconnecting from Gateway...");
        state.set(GatewayState.SHUTTING_DOWN);
        stopHeartbeat();
        if (webSocket != null) {
            webSocket.close(1000, "Client initiated disconnect");
            webSocket = null;
        }
        sequence.set(-1);
        sessionId = null;
        state.set(GatewayState.DISCONNECTED);
        if (connectionFuture != null && !connectionFuture.isDone()) {
            connectionFuture.completeExceptionally(new FluxException("Connection intentionally closed."));
        }
        LOGGER.info("Gateway disconnected.");
    }

    private void stopHeartbeat() {
        if (heartbeatTask != null) {
            heartbeatTask.cancel(true);
            heartbeatTask = null;
        }
        if (heartbeatExecutor != null) {
            if (!heartbeatExecutor.isShutdown()) {
                heartbeatExecutor.shutdownNow();
            }
            heartbeatExecutor = null;
        }
    }

    @Override
    public void send(String jsonPayload) {
        if (webSocket != null && state.get().isReadyToSend()) {
            webSocket.send(jsonPayload);
            return;
        }
        LOGGER.warn("Attempted to send payload while WebSocket is not ready or null. State: {}", state.get());
    }

    @Override
    public void sendPresenceUpdate(Object payloadData) {
        GatewaySendPayload gatewayPayload = new GatewaySendPayload(3, payloadData);
        send(jsonEngine.toJsonString(gatewayPayload));
    }

    private void sendIdentify() {
        LOGGER.debug("identify");
        state.set(GatewayState.IDENTIFYING);
        IdentifyPayload identifyData = new IdentifyPayload();
        identifyData.token = this.botToken;
        identifyData.intents = this.intentsBitmask;
        identifyData.properties = new IdentifyProperties();
        GatewaySendPayload payload = new GatewaySendPayload(2, identifyData);
        send(jsonEngine.toJsonString(payload));
    }

    private void sendResume() {
        LOGGER.info("Sending Resume payload for session ID: {}", sessionId);
        state.set(GatewayState.RESUMING);
        ResumePayload resumeData = new ResumePayload(this.botToken, this.sessionId, this.sequence.get());
        GatewaySendPayload payload = new GatewaySendPayload(6, resumeData);
        send(jsonEngine.toJsonString(payload));
    }

    private void sendHeartbeat() {
        if (!receivedHeartbeatAck.get()) {
            LOGGER.warn("Did not receive Heartbeat ACK since last heartbeat. Reconnecting (Zombied Connection)...");
            attemptReconnect(true);
            return;
        }
        receivedHeartbeatAck.set(false);
        int sequenceValue = sequence.get();
        GatewaySendPayload payload = new GatewaySendPayload(1, sequenceValue == -1 ? null : sequenceValue);
        send(jsonEngine.toJsonString(payload));
        LOGGER.trace("Heartbeat sent (s: {})", sequenceValue);
    }

    private void startHeartbeat(int intervalMillis) {
        stopHeartbeat();
        heartbeatExecutor = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "Flux-Heartbeat-Thread");
            t.setDaemon(true);
            return t;
        });
        LOGGER.debug("heartbeat every {}ms", intervalMillis);
        long initialDelay = (long) (intervalMillis * Math.random());
        heartbeatTask = heartbeatExecutor.scheduleAtFixedRate(this::sendHeartbeat, initialDelay, intervalMillis,
                TimeUnit.MILLISECONDS);
    }

    @Override
    public void sendVoiceStateUpdate(String guildId, @Nullable String channelId, boolean selfMute, boolean selfDeaf) {
        VoiceStateUpdatePayload data = new VoiceStateUpdatePayload(guildId, channelId, selfMute, selfDeaf);
        GatewaySendPayload payload = new GatewaySendPayload(4, data);
        send(jsonEngine.toJsonString(payload));
    }

    @Override
    public void playSoundboardSound(String guildId, String channelId, String soundId) {
        VoiceStateUpdatePayload data = new VoiceStateUpdatePayload(guildId, channelId, false, false);
        data.setSoundboardSoundId(soundId);
        GatewaySendPayload payload = new GatewaySendPayload(4, data);
        LOGGER.info("Sending play soundboard sound request for sound ID {}", soundId);
        send(jsonEngine.toJsonString(payload));
    }

    private enum GatewayState {
        DISCONNECTED, CONNECTING, RECONNECTING, IDENTIFYING, RESUMING, CONNECTED, SHUTTING_DOWN;

        public boolean isReadyToSend() {
            return this == CONNECTED || this == IDENTIFYING || this == RESUMING;
        }
    }

    public static class GatewayReceivePayload {
        @JsonProperty("op")
        public int op;
        @JsonProperty("d")
        public JsonNode d;
        @JsonProperty("s")
        public Integer s;
        @JsonProperty("t")
        public String t;
    }

    public static class GatewaySendPayload {
        @JsonProperty("op")
        public final int op;
        @JsonProperty("d")
        public final Object d;

        public GatewaySendPayload(int op, Object d) {
            this.op = op;
            this.d = d;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ActivitySendPayload {
        @JsonProperty("name")
        public final String name;
        @JsonProperty("type")
        public final int type;
        @JsonProperty("url")
        public final String url;

        public ActivitySendPayload(String name, int type, @Nullable String url) {
            if (name == null || name.trim().isEmpty())
                throw new IllegalArgumentException("Activity name cannot be null or empty.");
            this.name = name;
            this.type = type;
            if (type == 1 && (url == null || url.trim().isEmpty()))
                throw new IllegalArgumentException("Activity URL cannot be null or empty when type is STREAMING (1).");
            this.url = url;
        }
    }

    public record PresenceUpdateSendPayload(@JsonProperty("activities") List<ActivitySendPayload> activities,
            @JsonProperty("afk") boolean afk,
            @JsonProperty("since") @JsonInclude(JsonInclude.Include.ALWAYS) Long since,
            @JsonProperty("status") String status) {
    }

    private static class HelloPayload {
        @JsonProperty("heartbeat_interval")
        public int heartbeatInterval;
    }

    private static class IdentifyProperties {
        @JsonProperty("os")
        public String os = System.getProperty("os.name");
        @JsonProperty("browser")
        public String browser = "Fluxer4J";
        @JsonProperty("device")
        public String device = "Fluxer4J";
    }

    private static class IdentifyPayload {
        @JsonProperty("token")
        public String token;
        @JsonProperty("intents")
        public int intents;
        @JsonProperty("properties")
        public IdentifyProperties properties;
    }

    private class FluxWebSocketListener extends WebSocketListener {
        @Override
        public void onOpen(@NotNull WebSocket ws, @NotNull Response response) {
            LOGGER.debug("websocket opened");
            response.close();
        }

        @Override
        public void onMessage(@NotNull WebSocket ws, @NotNull String text) {
            LOGGER.trace("GATEWAY RECV <- {}", text);
            try {
                GatewayReceivePayload payload = jsonEngine.fromJsonString(text, GatewayReceivePayload.class);
                if (payload.s != null)
                    sequence.set(payload.s);

                switch (payload.op) {
                    case 0 -> handleDispatch(payload.t, payload.d);
                    case 1 -> {
                        LOGGER.debug("Gateway requested a heartbeat. Sending one now.");
                        sendHeartbeat();
                    }
                    case 7 -> {
                        LOGGER.warn("Received Opcode 7 (Reconnect). Attempting to reconnect and resume...");
                        attemptReconnect(true);
                    }
                    case 9 -> {
                        LOGGER.warn("Received Opcode 9 (Invalid Session). Resumable: {}", payload.d.asBoolean());
                        attemptReconnect(payload.d.asBoolean());
                    }
                    case 10 -> {
                        LOGGER.debug("hello from gateway");
                        receivedHeartbeatAck.set(true);
                        HelloPayload helloData = jsonEngine.fromJsonString(payload.d.toString(), HelloPayload.class);
                        startHeartbeat(helloData.heartbeatInterval);

                        if (sessionId != null && (state.get() == GatewayState.CONNECTING
                                || state.get() == GatewayState.RECONNECTING)) {
                            sendResume();
                        } else {
                            sendIdentify();
                        }
                    }
                    case 11 -> {
                        LOGGER.trace("Heartbeat ACK received.");
                        receivedHeartbeatAck.set(true);
                    }
                    default -> LOGGER.warn("Received unhandled opcode: {}", payload.op);
                }
            } catch (Exception e) {
                LOGGER.error("Error processing message from Gateway: {}", text, e);
            }
        }

        private void handleDispatch(String eventType, JsonNode eventDataNode) {
            if (eventDataNode == null) {
                LOGGER.warn("Received DISPATCH event {} with null data.", eventType);
                return;
            }
            String eventDataJson = eventDataNode.toString();
            FluxClient clientInstance = (FluxClient) eventDispatcher;

            try {
                Event event = null;
                switch (eventType) {
                    case "READY" -> {
                        ReadyPayloadData readyData = jsonEngine.fromJsonString(eventDataJson, ReadyPayloadData.class);
                        LOGGER.debug("gateway ready session {}", readyData.getSessionId());
                        state.set(GatewayState.CONNECTED);
                        sessionId = readyData.getSessionId();
                        resumeGatewayUrl = readyData.getResumeGatewayUrl();
                        if (connectionFuture != null && !connectionFuture.isDone()) {
                            connectionFuture.complete(null);
                        }
                        event = new ReadyEvent(clientInstance, readyData.getSelfUser(), readyData.getSessionId(),
                                resumeGatewayUrl, readyData.getGatewayVersion());
                    }
                    case "RESUMED" -> {
                        LOGGER.info("Successfully resumed session!");
                        state.set(GatewayState.CONNECTED);
                        if (connectionFuture != null && !connectionFuture.isDone()) {
                            connectionFuture.complete(null);
                        }
                    }
                    case "MESSAGE_CREATE" -> {
                        event = new MessageCreateEvent(clientInstance,
                                jsonEngine.fromJsonString(eventDataJson, MessageImpl.class));
                    }
                    case "MESSAGE_UPDATE" -> {
                        event = new MessageUpdateEvent(clientInstance,
                                jsonEngine.fromJsonString(eventDataJson, MessageImpl.class));
                    }
                    case "MESSAGE_DELETE" -> {
                        MessageDeletePayloadData deleteData = jsonEngine.fromJsonString(eventDataJson,
                                MessageDeletePayloadData.class);
                        event = new MessageDeleteEvent(clientInstance, deleteData.getId(), deleteData.getChannelId(),
                                deleteData.getGuildId());
                    }
                    case "GUILD_CREATE" -> {
                        GuildImpl guild = jsonEngine.fromJsonString(eventDataJson, GuildImpl.class);
                        guild.getRoles().forEach(role -> {
                            if (role instanceof RoleImpl r)
                                r.setGuildId(guild.getId());
                        });
                        guild.getVoiceStates().forEach(vs -> {
                            if (vs instanceof VoiceStateImpl v)
                                v.setGuildId(guild.getId());
                        });
                        event = new GuildCreateEvent(clientInstance, guild);
                        LOGGER.debug("guild create {}", guild.getName());
                    }
                    case "GUILD_MEMBER_ADD" -> {
                        MemberImpl memberAdded = jsonEngine.fromJsonString(eventDataJson, MemberImpl.class);
                        memberAdded.setFluxClient(clientInstance);
                        event = new GuildMemberAddEvent(clientInstance, memberAdded);
                    }
                    case "GUILD_MEMBER_UPDATE" -> {
                        MemberImpl updatedMember = jsonEngine.fromJsonString(eventDataJson, MemberImpl.class);
                        updatedMember.setFluxClient(clientInstance);
                        event = new GuildMemberUpdateEvent(clientInstance, updatedMember);
                    }
                    case "GUILD_MEMBER_REMOVE" -> {
                        GuildMemberRemovePayloadData removeData = jsonEngine.fromJsonString(eventDataJson,
                                GuildMemberRemovePayloadData.class);
                        event = new GuildMemberRemoveEvent(clientInstance, removeData.getGuildId(),
                                removeData.getUser());
                    }
                    case "VOICE_STATE_UPDATE" -> {
                        VoiceStatePayloadData vsData = jsonEngine.fromJsonString(eventDataJson,
                                VoiceStatePayloadData.class);
                        event = new VoiceStateUpdateEvent(clientInstance, vsData.getGuildId(), vsData.getChannelId(),
                                vsData.getUserId(), vsData.isMute() || vsData.isSelfMute(),
                                vsData.isDeaf() || vsData.isSelfDeaf());
                    }
                    case "VOICE_SERVER_UPDATE" -> {
                        JsonNode guildIdNode = eventDataNode.get("guild_id");
                        JsonNode tokenNode = eventDataNode.get("token");
                        JsonNode endpointNode = eventDataNode.get("endpoint");
                        if (guildIdNode != null && tokenNode != null && endpointNode != null) {
                            event = new VoiceServerUpdateEvent(clientInstance, guildIdNode.asText(), tokenNode.asText(),
                                    endpointNode.asText());
                        }
                    }
                    case "INTERACTION_CREATE" -> {
                        JsonNode typeNode = eventDataNode.get("type");
                        int type = typeNode != null ? typeNode.asInt() : 0;

                        if (type == 4) {
                            flux.model.interaction.AutocompleteInteractionImpl interaction = jsonEngine.fromJsonString(eventDataJson,
                                    flux.model.interaction.AutocompleteInteractionImpl.class);
                            interaction.setClient(clientInstance);
                            event = new InteractionCreateEvent(clientInstance, interaction);
                        } else {
                            SlashCommandInteractionImpl interaction = jsonEngine.fromJsonString(eventDataJson,
                                    SlashCommandInteractionImpl.class);
                            interaction.setClient(clientInstance);
                            event = new InteractionCreateEvent(clientInstance, interaction);
                        }
                    }
                    case "CHANNEL_UPDATE" -> {
                        JsonNode guildIdNode = eventDataNode.get("guild_id");
                        String guildId = guildIdNode != null ? guildIdNode.asText() : null;
                        flux.model.channel.VoiceChannelImpl channel = jsonEngine.fromJsonString(
                                eventDataJson,
                                flux.model.channel.VoiceChannelImpl.class);
                        event = new flux.api.event.channel.ChannelUpdateEvent(clientInstance, channel,
                                guildId);
                    }
                    default -> LOGGER.trace("Unhandled DISPATCH event type: {}", eventType);
                }
                if (event != null) {
                    eventDispatcher.dispatch(event);
                }
            } catch (Exception e) {
                LOGGER.error("Error handling DISPATCH event {} with data: {}", eventType, eventDataJson, e);
            }
        }

        @Override
        public void onMessage(@NotNull WebSocket ws, @NotNull ByteString bytes) {
            LOGGER.warn("Received binary message, which is not expected.");
        }

        @Override
        public void onClosing(@NotNull WebSocket ws, int code, @NotNull String reason) {
            LOGGER.warn("Gateway is closing connection: {} - {}", code, reason);
        }

        @Override
        public void onClosed(@NotNull WebSocket ws, int code, @NotNull String reason) {
            LOGGER.warn("Gateway connection closed: {} - {}", code, reason);
            boolean wasShuttingDown = state.get() == GatewayState.SHUTTING_DOWN;
            state.set(GatewayState.DISCONNECTED);
            stopHeartbeat();

            if (connectionFuture != null && !connectionFuture.isDone()) {
                connectionFuture.completeExceptionally(
                        new FluxException("Gateway connection closed unexpectedly: " + code + " " + reason));
            }

            boolean canResume = (code == 1001 || code < 4000 || code == 4008);
            if (!wasShuttingDown) {
                attemptReconnect(canResume);
            }
        }

        @Override
        public void onFailure(@NotNull WebSocket ws, @NotNull Throwable t, @Nullable Response response) {
            LOGGER.error("Gateway connection failure!", t);
            if (response != null) {
                response.close();
            }
            boolean wasShuttingDown = state.get() == GatewayState.SHUTTING_DOWN;
            state.set(GatewayState.DISCONNECTED);
            stopHeartbeat();

            if (connectionFuture != null && !connectionFuture.isDone()) {
                connectionFuture.completeExceptionally(t);
            }
            if (!wasShuttingDown) {
                attemptReconnect(true);
            }
        }
    }
}
