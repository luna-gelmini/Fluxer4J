package flux;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import flux.api.ApiEnvironment;
import flux.api.FluxClient;
import flux.api.entities.*;
import flux.api.entities.channel.Channel;
import flux.api.entities.channel.ChannelType;
import flux.api.event.Event;
import flux.api.event.EventListener;
import flux.api.event.voice.VoiceServerUpdateEvent;
import flux.api.exception.FluxException;
import flux.api.gateway.EventDispatcher;
import flux.api.gateway.GatewayIntent;
import flux.api.payload.channel.ChannelModifyPayload;
import flux.api.payload.channel.CreateGuildChannelPayload;
import flux.api.payload.interaction.CreateCommandPayload;
import flux.api.payload.member.ModifyMemberPayload;
import flux.api.payload.permission.Permission;
import flux.api.payload.permission.PermissionOverwritePayload;
import flux.api.payload.send.MessageSendPayload;
import flux.api.payload.moderation.BanPayload;
import flux.api.payload.moderation.BulkDeletePayload;
import flux.api.payload.moderation.TimeoutPayload;
import flux.api.voice.VoiceConnection;
import flux.gateway.client.GatewayClient;
import flux.gateway.client.RestClient;
import flux.gateway.client.impl.OkHttpWebSocketGatewayClientImpl;
import flux.json.util.JsonEngine;
import flux.model.gateway.ReadyEvent;
import flux.model.guild.GuildImpl;
import flux.model.member.MemberImpl;
import flux.model.message.MessageImpl;
import flux.model.role.RoleImpl;
import flux.model.user.UserImpl;
import flux.rest.FluxRest;
import flux.rest.Routes;
import flux.voice.VoiceConnectionImpl;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class FluxClientImpl implements FluxClient, EventDispatcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(FluxClientImpl.class);

    private final RestClient restClient;
    private final FluxRest rest;
    private final JsonEngine jsonEngine;
    private final List<EventListener> eventListeners = new CopyOnWriteArrayList<>();
    private final GatewayClient gatewayClient;
    private final OkHttpClient sharedOkHttpClient;
    private final Map<String, VoiceConnection> voiceConnections = new ConcurrentHashMap<>();
    private final Map<String, CompletableFuture<VoiceConnection>> pendingVoiceConnections = new ConcurrentHashMap<>();

    private boolean loggedIn = false;
    private User selfUser;
    private String sessionId;

    FluxClientImpl(RestClient restClient, JsonEngine jsonEngine, OkHttpClient sharedOkHttpClient) {
        this.restClient = Objects.requireNonNull(restClient, "RestClient cannot be null");
        this.jsonEngine = Objects.requireNonNull(jsonEngine, "JsonEngine cannot be null");
        this.sharedOkHttpClient = Objects.requireNonNull(sharedOkHttpClient, "Shared OkHttpClient cannot be null");
        this.gatewayClient = new OkHttpWebSocketGatewayClientImpl(this.sharedOkHttpClient, this.jsonEngine, this);
        this.rest = new FluxRest(this.restClient);
    }

    @Override
    public FluxRest rest() {
        return rest;
    }

    @Override
    public CompletableFuture<Void> login(String token, Collection<GatewayIntent> intents) {
        if (loggedIn) {
            return CompletableFuture.failedFuture(new FluxException("Already logged in."));
        }
        String botToken = Objects.requireNonNull(token, "Token cannot be null");
        this.restClient.setBotToken(botToken);
        this.gatewayClient.setBotToken(botToken);
        this.gatewayClient.setIntents(intents);

        LOGGER.info("FluxClient: Token and intents set. Resolving Gateway URL...");

        return resolveGatewayUrl(botToken).thenCompose(gatewayUrl -> {
            gatewayClient.setGatewayUrl(gatewayUrl);
            LOGGER.info("Connecting to Gateway at {}...", gatewayUrl);
            return gatewayClient.connect();
        }).thenRun(() -> {
            loggedIn = true;
            LOGGER.info("FluxClient successfully connected to Gateway and received READY.");
        }).exceptionally(throwable -> {
            LOGGER.error("FluxClient login failed during Gateway connection.", throwable);

            if (throwable instanceof CompletionException && throwable.getCause() != null) {
                throw new FluxException("Gateway login failed", throwable.getCause());
            }
            throw new FluxException("Gateway login failed", throwable);
        });
    }

    private CompletableFuture<String> resolveGatewayUrl(String botToken) {
        String configuredGateway = ApiEnvironment.getGatewayUrl();
        String url = Routes.gatewayBot();
        return restClient.get(url, Map.of("Authorization", "Bot " + botToken))
                .thenApply(response -> {
                    JsonNode node = jsonEngine.fromJsonString(response, JsonNode.class);
                    JsonNode gatewayNode = node.get("url");
                    if (gatewayNode == null || gatewayNode.asText().isBlank()) {
                        LOGGER.warn("Gateway discovery returned no URL; using configured default: {}", configuredGateway);
                        return configuredGateway;
                    }
                    String discovered = gatewayNode.asText();
                    LOGGER.info("Gateway URL discovered: {}", discovered);
                    return discovered;
                })
                .exceptionally(throwable -> {
                    LOGGER.warn("Failed to discover Gateway URL ({}). Using configured default: {}",
                            throwable.getMessage(), configuredGateway);
                    return configuredGateway;
                });
    }

    @Override
    public void setActivity(ActivityType type, String name, @Nullable String url) {
        if (!loggedIn) {
            LOGGER.warn("Cannot set activity, client is not logged in.");
            return;
        }

        try {

            OkHttpWebSocketGatewayClientImpl.ActivitySendPayload activity = new OkHttpWebSocketGatewayClientImpl.ActivitySendPayload(
                    name, type.getValue(), url);
            OkHttpWebSocketGatewayClientImpl.PresenceUpdateSendPayload presence = new OkHttpWebSocketGatewayClientImpl.PresenceUpdateSendPayload(
                    List.of(activity),
                    false,
                    null,
                    "online");

            gatewayClient.sendPresenceUpdate(presence);

        } catch (IllegalArgumentException e) {

            LOGGER.error("Failed to set activity due to invalid arguments: {}", e.getMessage());
        }
    }

    @Override
    public void shutdown() {
        if (!loggedIn)
            return;
        LOGGER.info("FluxClient shutting down...");
        voiceConnections.keySet().forEach(this::leaveVoiceChannel);
        gatewayClient.disconnect();
        restClient.shutdown();
        loggedIn = false;
        LOGGER.info("FluxClient shutdown complete.");
    }

    @Override
    public User getSelfUser() {
        if (this.selfUser == null) {
            throw new IllegalStateException("Self user is not available yet. Bot might not be fully ready.");
        }
        return this.selfUser;
    }

    @Override
    public void dispatch(Event event) {
        if (event instanceof ReadyEvent readyEvent) {
            this.selfUser = readyEvent.getSelfUser();
            this.sessionId = readyEvent.getSessionId();
            LOGGER.info("Client is READY. Session ID set to {}", this.sessionId);
        }

        if (event instanceof VoiceServerUpdateEvent vsu) {
            LOGGER.debug("Received VoiceServerUpdate for guild {}. Checking for pending connections.",
                    vsu.getGuildId());
            CompletableFuture<VoiceConnection> pendingFuture = pendingVoiceConnections.get(vsu.getGuildId());
            VoiceConnection conn = voiceConnections.get(vsu.getGuildId());

            if (pendingFuture != null && conn instanceof VoiceConnectionImpl voiceConn) {
                LOGGER.info("Found pending voice connection for guild {}. Initiating voice server connection.",
                        vsu.getGuildId());
                voiceConn.connect(this.sessionId, vsu.getToken(), vsu.getEndpoint())
                        .whenComplete((aVoid, throwable) -> {
                            pendingVoiceConnections.remove(vsu.getGuildId());
                            if (throwable != null) {
                                LOGGER.error("Failed to establish voice connection for guild {}", vsu.getGuildId(),
                                        throwable);
                                pendingFuture.completeExceptionally(throwable);
                                voiceConnections.remove(vsu.getGuildId());
                                conn.disconnect();
                            } else {
                                LOGGER.info("Voice connection for guild {} fully established.", vsu.getGuildId());
                                pendingFuture.complete(conn);
                            }
                        });
            }
        }

        for (EventListener listener : eventListeners) {
            try {
                listener.onEvent(event);
            } catch (Exception e) {
                LOGGER.error("Uncaught exception in event listener {} for event {}: {}",
                        listener.getClass().getName(), event.getClass().getName(), e.getMessage(), e);
            }
        }
    }

    @Override
    public CompletableFuture<VoiceConnection> joinVoiceChannel(String guildId, String channelId) {
        if (!loggedIn) {
            return CompletableFuture
                    .failedFuture(new FluxException("Cannot join voice channel, client is not logged in."));
        }
        return pendingVoiceConnections.computeIfAbsent(guildId, gid -> {
            LOGGER.info("Initiating join for guild {}, channel {}.", gid, channelId);

            if (voiceConnections.containsKey(gid)) {
                pendingVoiceConnections.remove(gid);
                return CompletableFuture.completedFuture(voiceConnections.get(gid));
            }

            CompletableFuture<VoiceConnection> connectionFuture = new CompletableFuture<>();
            VoiceConnectionImpl connection = new VoiceConnectionImpl(gid, getSelfUser().getId(), this, jsonEngine,
                    sharedOkHttpClient);
            voiceConnections.put(gid, connection);

            gatewayClient.sendVoiceStateUpdate(gid, channelId, false, false);

            return connectionFuture;
        });
    }

    @Override
    public CompletableFuture<Void> sendTyping(String channelId) {
        if (!loggedIn) {
            return CompletableFuture.failedFuture(new FluxException("Not logged in. Call login() first."));
        }
        Objects.requireNonNull(channelId, "Channel ID cannot be null");

        String url = Routes.typing(channelId);
        LOGGER.debug("Sending typing indicator to channel: {}", channelId);

        return restClient.post(url, "{}", Collections.emptyMap())
                .thenAccept(responseBody -> LOGGER.debug("Typing indicator sent successfully to channel: {}",
                        channelId))
                .exceptionally(throwable -> {
                    LOGGER.error("Failed to send typing indicator to channel {}: {}", channelId, throwable.getMessage(),
                            throwable);
                    throw new FluxException("Failed to send typing indicator", throwable);
                });
    }

    @Override
    public void _internal_leaveVoiceChannel(String guildId) {

        this.leaveVoiceChannel(guildId);
    }

    @Override
    public CompletableFuture<Void> leaveVoiceChannel(String guildId) {
        if (!loggedIn) {
            LOGGER.warn("Cannot leave voice channel, client is not logged in.");
            return CompletableFuture.failedFuture(new FluxException("Not logged in."));
        }
        LOGGER.info("Leaving voice channel in guild {}.", guildId);
        gatewayClient.sendVoiceStateUpdate(guildId, null, false, false);

        VoiceConnection connection = voiceConnections.remove(guildId);
        if (connection != null) {

            return connection.disconnect();
        }

        CompletableFuture<VoiceConnection> pending = pendingVoiceConnections.remove(guildId);
        if (pending != null && !pending.isDone()) {
            pending.completeExceptionally(
                    new FluxException("Voice connection was cancelled by leaving the channel."));
        }

        return CompletableFuture.completedFuture(null);
    }

    @Override
    public void playSoundboardSound(String guildId, String channelId, String soundId) {
        if (!loggedIn) {
            LOGGER.warn("Cannot play soundboard sound, client is not logged in.");
            return;
        }
        Objects.requireNonNull(guildId, "Guild ID cannot be null");
        Objects.requireNonNull(channelId, "Channel ID cannot be null");
        Objects.requireNonNull(soundId, "Sound ID cannot be null");

        gatewayClient.playSoundboardSound(guildId, channelId, soundId);
    }

    @Override
    public void sendVoiceStateUpdate(String guildId, @Nullable String channelId, boolean selfMute, boolean selfDeaf) {
        if (!loggedIn) {
            LOGGER.warn("Cannot send voice state update, client is not logged in.");
            return;
        }
        gatewayClient.sendVoiceStateUpdate(guildId, channelId, selfMute, selfDeaf);
    }

    @Override
    public CompletableFuture<Channel> modifyChannel(String channelId, ChannelModifyPayload payload) {
        if (!loggedIn) {
            return CompletableFuture.failedFuture(new FluxException("Not logged in. Call login() first."));
        }
        Objects.requireNonNull(channelId, "Channel ID cannot be null");
        Objects.requireNonNull(payload, "Payload cannot be null");

        String url = Routes.channel(channelId);
        String jsonPayload = jsonEngine.toJsonString(payload);
        LOGGER.debug("Modifying channel {}: {}", channelId, jsonPayload);

        return restClient.patch(url, jsonPayload, Collections.emptyMap())
                .thenApply(responseBody -> {
                    LOGGER.debug("Received response for modifyChannel: {}", responseBody);
                    return jsonEngine.fromJsonString(responseBody, Channel.class);
                })
                .exceptionally(throwable -> {
                    LOGGER.error("Failed to modify channel {}: {}", channelId, throwable.getMessage(), throwable);
                    throw new FluxException("Failed to modify channel " + channelId, throwable);
                });
    }

    public CompletableFuture<Void> editChannelPermissions(String channelId, String targetId, TargetType type,
            Collection<Permission> allow, Collection<Permission> deny) {

        long allowBitmask = Permission.calculateBitmask(allow);
        long denyBitmask = Permission.calculateBitmask(deny);
        return editChannelPermissions(channelId, targetId, type, allowBitmask, denyBitmask);
    }

    @Override
    public CompletableFuture<Void> editChannelPermissions(String channelId, String targetId, TargetType type,
            long allowBitmask, long denyBitmask) {
        if (!loggedIn) {
            return CompletableFuture.failedFuture(new FluxException("Not logged in. Call login() first."));
        }
        Objects.requireNonNull(channelId, "Channel ID cannot be null");
        Objects.requireNonNull(targetId, "Target ID cannot be null");
        Objects.requireNonNull(type, "Target type cannot be null");

        String url = Routes.channelPermission(channelId, targetId);

        PermissionOverwritePayload payload = new PermissionOverwritePayload(
                type.getValue(),
                String.valueOf(allowBitmask),
                String.valueOf(denyBitmask));
        String jsonPayload = jsonEngine.toJsonString(payload);
        LOGGER.debug("Editing permissions for target {} in channel {}: {}", targetId, channelId, jsonPayload);

        return restClient.put(url, jsonPayload, Collections.emptyMap())
                .thenAccept(responseBody -> LOGGER.info("Permissions for target {} in channel {} edited successfully.",
                        targetId, channelId))
                .exceptionally(throwable -> {
                    LOGGER.error("Failed to edit permissions for target {} in channel {}: {}", targetId, channelId,
                            throwable.getMessage(), throwable);
                    throw new FluxException("Failed to edit channel permissions", throwable);
                });
    }

    @Override
    public CompletableFuture<Channel> createGuildChannel(String guildId, String name, ChannelType type,
            @Nullable String parentCategoryId) {
        if (!loggedIn) {
            return CompletableFuture.failedFuture(new FluxException("Not logged in. Call login() first."));
        }
        Objects.requireNonNull(guildId, "Guild ID cannot be null");
        Objects.requireNonNull(name, "Channel name cannot be null");
        Objects.requireNonNull(type, "Channel type cannot be null");

        String url = Routes.guildChannels(guildId);
        CreateGuildChannelPayload payload = new CreateGuildChannelPayload(name, type);
        if (parentCategoryId != null) {
            payload.setParentId(parentCategoryId);
        }

        String jsonPayload = jsonEngine.toJsonString(payload);
        LOGGER.debug("Creating guild channel in {}: {}", guildId, jsonPayload);

        return restClient.post(url, jsonPayload, Collections.emptyMap())
                .thenApply(responseBody -> {
                    LOGGER.debug("Received response for createGuildChannel: {}", responseBody);
                    Channel createdChannel = jsonEngine.fromJsonString(responseBody, Channel.class);
                    createdChannel.setFluxClient(this);
                    return createdChannel;
                })
                .exceptionally(throwable -> {
                    throw new FluxException("createChannel failed", throwable);
                });
    }

    @Override
    public CompletableFuture<Channel> deleteChannel(String channelId) {
        if (!loggedIn) {
            return CompletableFuture.failedFuture(new FluxException("Not logged in. Call login() first."));
        }
        Objects.requireNonNull(channelId, "Channel ID cannot be null");

        String url = Routes.channel(channelId);
        LOGGER.debug("Deleting channel: {}", channelId);

        return restClient.delete(url, Collections.emptyMap())
                .thenApply(responseBody -> {
                    LOGGER.debug("Received response for deleteChannel: {}", responseBody);
                    Channel deletedChannel = jsonEngine.fromJsonString(responseBody, Channel.class);
                    deletedChannel.setFluxClient(this);
                    return deletedChannel;
                })
                .exceptionally(throwable -> {
                    throw new FluxException("deleteChannel failed", throwable);
                });
    }

    public CompletableFuture<Void> modifyGuildMemberVoiceChannel(String guildId, String userId,
            @Nullable String voiceChannelId) {
        if (!loggedIn) {
            return CompletableFuture.failedFuture(new FluxException("Not logged in. Call login() first."));
        }
        Objects.requireNonNull(guildId, "Guild ID cannot be null");
        Objects.requireNonNull(userId, "User ID cannot be null");

        String url = Routes.guildMember(guildId, userId);
        ModifyMemberPayload payload = new ModifyMemberPayload(voiceChannelId);
        String jsonPayload = jsonEngine.toJsonString(payload);
        LOGGER.debug("Modifying guild member {} in {}. Setting voice channel to: {}", userId, guildId, voiceChannelId);

        return restClient.patch(url, jsonPayload, Collections.emptyMap())
                .thenAccept(responseBody -> LOGGER
                        .info("Successfully modified voice channel for member {} in guild {}.", userId, guildId))
                .exceptionally(throwable -> {
                    LOGGER.error("Failed to modify voice channel for member {} in guild {}: {}", userId, guildId,
                            throwable.getMessage(), throwable);
                    throw new FluxException("Failed to modify member's voice channel", throwable);
                });
    }

    @Override
    public CompletableFuture<Channel> createGuildChannel(String guildId, CreateGuildChannelPayload payload) {
        if (!loggedIn) {
            return CompletableFuture.failedFuture(new FluxException("Not logged in. Call login() first."));
        }
        Objects.requireNonNull(guildId, "Guild ID cannot be null");
        Objects.requireNonNull(payload, "Payload cannot be null");

        String url = Routes.guildChannels(guildId);
        String jsonPayload = jsonEngine.toJsonString(payload);
        LOGGER.debug("Creating guild channel in {}: {}", guildId, jsonPayload);

        return restClient.post(url, jsonPayload, Collections.emptyMap())
                .thenApply(responseBody -> {
                    LOGGER.debug("Received response for createGuildChannel: {}", responseBody);
                    return jsonEngine.fromJsonString(responseBody, Channel.class);
                })
                .exceptionally(throwable -> {
                    LOGGER.error("Failed to create guild channel in {}: {}", guildId, throwable.getMessage(),
                            throwable);
                    throw new FluxException("Failed to create guild channel", throwable);
                });
    }

    @Override
    public CompletableFuture<Message> sendMessage(String channelId, String content) {
        if (!loggedIn) {
            return CompletableFuture.failedFuture(new FluxException("Not logged in. Call login() first."));
        }
        Objects.requireNonNull(channelId, "Channel ID cannot be null");
        Objects.requireNonNull(content, "Message content cannot be null");
        if (content.isEmpty()) {
            return CompletableFuture.failedFuture(new IllegalArgumentException("Message content cannot be empty."));
        }
        if (content.length() > 2000) {
            return CompletableFuture
                    .failedFuture(new IllegalArgumentException("Message content cannot exceed 2000 characters."));
        }

        String url = Routes.channelMessages(channelId);
        Map<String, String> payload = Collections.singletonMap("content", content);
        String jsonPayload = jsonEngine.toJsonString(payload);

        LOGGER.debug("Sending message to channel {}: {}", channelId, jsonPayload);

        return restClient.post(url, jsonPayload, Collections.emptyMap())
                .thenApply(responseBody -> {
                    LOGGER.debug("Received response for sendMessage: {}", responseBody);
                    return (Message) jsonEngine.fromJsonString(responseBody, MessageImpl.class);
                })
                .exceptionally(throwable -> {
                    LOGGER.error("Failed to send message to channel {}: {}", channelId, throwable.getMessage(),
                            throwable);
                    if (throwable instanceof FluxException) {
                        if (throwable.getCause() instanceof FluxException)
                            throw (FluxException) throwable.getCause();
                        throw new FluxException("Failed to send message", throwable);
                    }
                    throw new FluxException("Failed to send message", throwable);
                });
    }

    @Override
    public CompletableFuture<Message> sendMessage(String channelId, MessageSendPayload messageData) {
        if (!loggedIn) {
            return CompletableFuture.failedFuture(new FluxException("Not logged in. Call login() first."));
        }
        Objects.requireNonNull(channelId, "Channel ID cannot be null");
        Objects.requireNonNull(messageData, "MessageData (from API module) cannot be null");

        String url = Routes.channelMessages(channelId);
        String jsonPayload = jsonEngine.toJsonString(messageData);

        if (messageData.getAttachments() != null && !messageData.getAttachments().isEmpty()) {
            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            builder.addFormDataPart("payload_json", jsonPayload);

            int i = 0;
            for (Map.Entry<String, byte[]> entry : messageData.getAttachments().entrySet()) {
                builder.addFormDataPart("files[" + i + "]", entry.getKey(),
                        okhttp3.RequestBody.create(entry.getValue(),
                                okhttp3.MediaType.parse("application/octet-stream")));
                i++;
            }

            LOGGER.debug("Sending multipart message to channel {}: {} attachments", channelId,
                    messageData.getAttachments().size());
            return restClient.postMultipart(url, builder.build(), Collections.emptyMap())
                    .thenApply(responseBody -> (Message) jsonEngine.fromJsonString(responseBody, MessageImpl.class))
                    .exceptionally(throwable -> {
                        LOGGER.error("Failed to send multipart message to channel {}: {}", channelId,
                                throwable.getMessage(), throwable);
                        throw new FluxException("Failed to send multipart message", throwable);
                    });
        }

        LOGGER.debug("Sending message to channel {}: {}", channelId, jsonPayload);

        return restClient.post(url, jsonPayload, Collections.emptyMap())
                .thenApply(responseBody -> {
                    LOGGER.debug("Received response for sendMessage: {}", responseBody);
                    return (Message) jsonEngine.fromJsonString(responseBody, MessageImpl.class);
                })
                .exceptionally(throwable -> {
                    LOGGER.error("Failed to send message to channel {}: {}", channelId,
                            throwable.getMessage(), throwable);
                    throw new FluxException("Failed to send message from API payload", throwable);
                });
    }

    @Override
    public CompletableFuture<Message> editMessage(String channelId, String messageId, MessageSendPayload messageData) {
        if (!loggedIn) {
            return CompletableFuture.failedFuture(new FluxException("Not logged in. Call login() first."));
        }
        Objects.requireNonNull(channelId, "Channel ID cannot be null");
        Objects.requireNonNull(messageId, "Message ID cannot be null");
        Objects.requireNonNull(messageData, "MessageData cannot be null");

        String url = Routes.channelMessage(channelId, messageId);
        String jsonPayload = jsonEngine.toJsonString(messageData);

        LOGGER.debug("Editing message {} in channel {}: {}", messageId, channelId, jsonPayload);

        return restClient.patch(url, jsonPayload, Collections.emptyMap())
                .thenApply(responseBody -> {
                    LOGGER.debug("Received response for editMessage: {}", responseBody);
                    return (Message) jsonEngine.fromJsonString(responseBody, MessageImpl.class);
                })
                .exceptionally(throwable -> {
                    LOGGER.error("Failed to edit message {} in channel {}: {}", messageId, channelId,
                            throwable.getMessage(), throwable);
                    throw new FluxException("Failed to edit message", throwable);
                });
    }

    @Override
    public CompletableFuture<Message> sendMessage(String channelId, MultipartBody body) {
        if (!loggedIn) {
            return CompletableFuture.failedFuture(new FluxException("Not logged in. Call login() first."));
        }
        Objects.requireNonNull(channelId, "Channel ID cannot be null");
        Objects.requireNonNull(body, "MultipartBody cannot be null");

        String url = Routes.channelMessages(channelId);

        return restClient.postMultipart(url, body, Collections.emptyMap())
                .thenApply(responseBody -> jsonEngine.fromJsonString(responseBody, Message.class))
                .exceptionally(throwable -> {
                    LOGGER.error("Failed to send multipart message to channel {}: {}", channelId,
                            throwable.getMessage(), throwable);
                    throw new FluxException("Failed to send multipart message", throwable);
                });
    }

    @Override
    public void addEventListener(EventListener listener) {
        eventListeners.add(Objects.requireNonNull(listener, "Listener cannot be null"));
    }

    @Override
    public void removeEventListener(EventListener listener) {
        eventListeners.remove(Objects.requireNonNull(listener, "Listener cannot be null"));
    }

    @Override
    public CompletableFuture<User> getUserById(String userId) {
        if (!loggedIn) {
            return CompletableFuture.failedFuture(new FluxException("Not logged in. Call login() first."));
        }
        Objects.requireNonNull(userId, "User ID cannot be null");

        String url = Routes.user(userId);
        LOGGER.debug("Fetching user by ID: {}", userId);

        return restClient.get(url, Collections.emptyMap())
                .thenApply(responseBody -> {
                    LOGGER.debug("Received response for getUserById ({}): {}", userId, responseBody);
                    return (User) jsonEngine.fromJsonString(responseBody, UserImpl.class);
                })
                .exceptionally(throwable -> {
                    LOGGER.error("Failed to get user by ID {}: {}", userId, throwable.getMessage(), throwable);
                    throw new FluxException("Failed to get user by ID " + userId, throwable);
                });
    }

    @Override
    public CompletableFuture<Channel> getChannelById(String channelId) {
        if (!loggedIn) {
            return CompletableFuture.failedFuture(new FluxException("Not logged in. Call login() first."));
        }
        Objects.requireNonNull(channelId, "Channel ID cannot be null");

        String url = Routes.channel(channelId);
        LOGGER.debug("Fetching channel by ID: {}", channelId);

        return restClient.get(url, Collections.emptyMap())
                .thenApply(responseBody -> {
                    LOGGER.debug("Received response for getChannelById ({}): {}", channelId, responseBody);
                    Channel fetchedChannel = jsonEngine.fromJsonString(responseBody, Channel.class);
                    fetchedChannel.setFluxClient(this);
                    return fetchedChannel;
                })
                .exceptionally(throwable -> {
                    LOGGER.error("Failed to get channel by ID {}: {}", channelId, throwable.getMessage(), throwable);
                    throw new FluxException("Failed to get channel by ID " + channelId, throwable);
                });
    }

    @Override
    public CompletableFuture<Guild> getGuildById(String guildId) {
        if (!loggedIn) {
            return CompletableFuture.failedFuture(new FluxException("Not logged in. Call login() first."));
        }
        Objects.requireNonNull(guildId, "Guild ID cannot be null");

        String url = Routes.guild(guildId);
        LOGGER.debug("Fetching guild by ID: {}", guildId);

        return restClient.get(url, Collections.emptyMap())
                .thenApply(responseBody -> {
                    LOGGER.debug("Received response for getGuildById ({}): {}", guildId, responseBody);
                    GuildImpl guild = jsonEngine.fromJsonString(responseBody, GuildImpl.class);

                    if (guild.getRoles() != null) {
                        guild.getRoles().forEach(role -> {
                            if (role != null) {
                                ((RoleImpl) role).setGuildId(guildId);
                            }
                        });
                    }
                    return (Guild) guild;
                })
                .exceptionally(throwable -> {
                    LOGGER.error("Failed to get guild by ID {}: {}", guildId, throwable.getMessage(), throwable);
                    throw new FluxException("Failed to get guild by ID " + guildId, throwable);
                });
    }

    @Override
    public CompletableFuture<List<Role>> getGuildRoles(String guildId) {
        if (!loggedIn) {
            return CompletableFuture.failedFuture(new FluxException("Not logged in. Call login() first."));
        }
        Objects.requireNonNull(guildId, "Guild ID cannot be null");

        String url = Routes.guildRoles(guildId);
        LOGGER.debug("Fetching roles for guild ID: {}", guildId);

        return restClient.get(url, Collections.emptyMap())
                .thenApply(responseBody -> {
                    LOGGER.debug("Received response for getGuildRoles ({}): {}", guildId, responseBody);
                    List<RoleImpl> roleImpls = jsonEngine.fromJsonString(responseBody, new TypeReference<>() {
                    });
                    roleImpls.forEach(role -> role.setGuildId(guildId));
                    return (List<Role>) new ArrayList<Role>(roleImpls);
                })
                .exceptionally(throwable -> {
                    LOGGER.error("Failed to get roles for guild ID {}: {}", guildId, throwable.getMessage(), throwable);
                    throw new FluxException("Failed to get roles for guild ID " + guildId, throwable);
                });
    }

    @Override
    public CompletableFuture<Member> getGuildMember(String guildId, String userId) {
        if (!loggedIn) {
            return CompletableFuture.failedFuture(new FluxException("Not logged in. Call login() first."));
        }
        Objects.requireNonNull(guildId, "Guild ID cannot be null");
        Objects.requireNonNull(userId, "User ID cannot be null");

        String url = Routes.guildMember(guildId, userId);
        LOGGER.debug("Fetching member for guild {} and user {}:", guildId, userId);

        return restClient.get(url, Collections.emptyMap())
                .thenApply(responseBody -> {
                    LOGGER.debug("Received response for getGuildMember (guild: {}, user: {}): {}", guildId, userId,
                            responseBody);
                    MemberImpl member = jsonEngine.fromJsonString(responseBody, MemberImpl.class);
                    member.setGuildId(guildId);
                    member.setFluxClient(this);
                    return (Member) member;
                })
                .exceptionally(throwable -> {
                    LOGGER.error("Failed to get member for guild {} and user {}: {}", guildId, userId,
                            throwable.getMessage(), throwable);
                    throw new FluxException("Failed to get member (guild: " + guildId + ", user: " + userId + ")",
                            throwable);
                });
    }

    @Override
    public CompletableFuture<Void> addRoleToMember(String guildId, String userId, String roleId) {
        if (!loggedIn) {
            return CompletableFuture.failedFuture(new FluxException("Not logged in. Call login() first."));
        }
        Objects.requireNonNull(guildId, "Guild ID cannot be null");
        Objects.requireNonNull(userId, "User ID cannot be null");
        Objects.requireNonNull(roleId, "Role ID cannot be null");

        String url = Routes.guildMemberRole(guildId, userId, roleId);
        LOGGER.debug("Adding role {} to member {} in guild {}", roleId, userId, guildId);

        return restClient.put(url, null, Collections.emptyMap())
                .thenAccept(responseBody -> LOGGER.info("Role {} added to member {} in guild {} successfully.", roleId,
                        userId, guildId))
                .exceptionally(throwable -> {
                    LOGGER.error("Failed to add role {} to member {} in guild {}: {}", roleId, userId, guildId,
                            throwable.getMessage(), throwable);
                    throw new FluxException("Failed to add role to member", throwable);
                });
    }

    @Override
    public CompletableFuture<Void> removeRoleFromMember(String guildId, String userId, String roleId) {
        if (!loggedIn) {
            return CompletableFuture.failedFuture(new FluxException("Not logged in. Call login() first."));
        }
        Objects.requireNonNull(guildId, "Guild ID cannot be null");
        Objects.requireNonNull(userId, "User ID cannot be null");
        Objects.requireNonNull(roleId, "Role ID cannot be null");

        String url = Routes.guildMemberRole(guildId, userId, roleId);
        LOGGER.debug("Removing role {} from member {} in guild {}", roleId, userId, guildId);

        return restClient.delete(url, Collections.emptyMap())
                .thenAccept(responseBody -> LOGGER.info("Role {} removed from member {} in guild {} successfully.",
                        roleId, userId, guildId))
                .exceptionally(throwable -> {
                    LOGGER.error("Failed to remove role {} from member {} in guild {}: {}", roleId, userId, guildId,
                            throwable.getMessage(), throwable);
                    throw new FluxException("Failed to remove role from member", throwable);
                });
    }

    @Override
    public CompletableFuture<Void> banMember(String guildId, String userId, String reason, int deleteMessageSeconds) {
        BanPayload payload = new BanPayload(deleteMessageSeconds, reason);
        return restClient.put(Routes.guildBan(guildId, userId), jsonEngine.toJsonString(payload), Map.of())
                .thenApply(response -> null);
    }

    @Override
    public CompletableFuture<Void> kickMember(String guildId, String userId, String reason) {
        Map<String, String> headers = new java.util.HashMap<>();
        if (reason != null) {
            headers.put("X-Audit-Log-Reason", reason);
        }
        return restClient.delete(Routes.guildMember(guildId, userId), headers).thenApply(response -> null);
    }

    @Override
    public CompletableFuture<Void> timeoutMember(String guildId, String userId, Integer durationSeconds,
            String reason) {
        String isoTimestamp = durationSeconds == null ? null
                : java.time.Instant.now().plusSeconds(durationSeconds).toString();
        TimeoutPayload payload = new TimeoutPayload(isoTimestamp);
        Map<String, String> headers = new java.util.HashMap<>();
        if (reason != null) {
            headers.put("X-Audit-Log-Reason", reason);
        }
        return restClient.patch(Routes.guildMember(guildId, userId), jsonEngine.toJsonString(payload), headers)
                .thenApply(response -> null);
    }

    @Override
    public CompletableFuture<Void> bulkDeleteMessages(String channelId, List<String> messageIds) {
        BulkDeletePayload payload = new BulkDeletePayload(messageIds);
        return restClient.post(Routes.bulkDeleteMessages(channelId), jsonEngine.toJsonString(payload), Map.of())
                .thenApply(response -> null);
    }

    @Override
    public CompletableFuture<Void> deleteMessage(String channelId, String messageId) {
        if (!loggedIn) {
            return CompletableFuture.failedFuture(new FluxException("Not logged in. Call login() first."));
        }
        Objects.requireNonNull(channelId, "Channel ID cannot be null");
        Objects.requireNonNull(messageId, "Message ID cannot be null");

        String url = Routes.channelMessage(channelId, messageId);
        LOGGER.debug("Deleting message {} in channel {}", messageId, channelId);

        return restClient.delete(url, Collections.emptyMap())
                .thenRun(() -> {})
                .exceptionally(throwable -> {
                    LOGGER.error("Failed to delete message {} in channel {}: {}", messageId, channelId,
                            throwable.getMessage(), throwable);
                    throw new FluxException("Failed to delete message", throwable);
                });
    }

    @Override
    public CompletableFuture<List<Message>> getMessages(String channelId, int limit) {
        if (!loggedIn) {
            return CompletableFuture.failedFuture(new FluxException("Not logged in. Call login() first."));
        }
        Objects.requireNonNull(channelId, "Channel ID cannot be null");
        if (limit < 1 || limit > 100) {
            return CompletableFuture.failedFuture(new IllegalArgumentException("Limit must be between 1 and 100."));
        }

        return restClient.get(Routes.channelMessages(channelId, limit), Map.of())
                .thenApply(responseBody -> {
                    List<MessageImpl> messageImpls = jsonEngine.fromJsonString(responseBody, new TypeReference<>() {
                    });
                    messageImpls.forEach(msg -> msg.setFluxClient(this));
                    return (List<Message>) new ArrayList<Message>(messageImpls);
                })
                .exceptionally(throwable -> {
                    throw new FluxException("Failed to get messages", throwable);
                });
    }

    @Override
    public CompletableFuture<Void> createInteractionResponse(String interactionId, String interactionToken,
            String jsonPayload) {
        LOGGER.warn("Fluxer OpenAPI has no interaction callback route; dropping interaction response.");
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture<Void> createGlobalCommand(CreateCommandPayload payload) {
        LOGGER.warn("Fluxer OpenAPI has no application command routes; skipping global command registration.");
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture<Void> createGuildCommand(String guildId, CreateCommandPayload payload) {
        LOGGER.warn("Fluxer OpenAPI has no application command routes; skipping guild command registration.");
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture<Void> bulkOverwriteGlobalCommands(List<CreateCommandPayload> payloads) {
        LOGGER.warn("Fluxer OpenAPI has no application command routes; skipping slash command sync.");
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture<String> sendInteractionFollowup(String applicationId, String token,
            okhttp3.MultipartBody body) {
        return restClient.postMultipart(Routes.webhookExecute(applicationId, token), body, Map.of());
    }
}
