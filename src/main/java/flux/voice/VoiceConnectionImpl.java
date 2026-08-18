package flux.voice;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.iwebpp.crypto.TweetNaclFast;
import flux.api.FluxClient;
import flux.api.entities.User;
import flux.api.voice.VoiceConnection;
import flux.json.util.JsonEngine;
import flux.opus.OpusDecoder;
import okhttp3.*;
import org.bitlet.weupnp.GatewayDevice;
import org.bitlet.weupnp.GatewayDiscover;
import org.bitlet.weupnp.PortMappingEntry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class VoiceConnectionImpl implements VoiceConnection {
    private static final Logger LOGGER = LoggerFactory.getLogger(VoiceConnectionImpl.class);
    private final String guildId;
    private final String userId;
    private final FluxClient client;
    private final JsonEngine jsonEngine;
    private final OkHttpClient httpClient;
    private final ExecutorService voiceExecutor = Executors.newSingleThreadExecutor(r -> new Thread(r, "Flux-Voice-Thread"));
    private final CountDownLatch secretKeyLatch = new CountDownLatch(1);
    private final Map<Integer, OpusDecoder> opusDecoders = new ConcurrentHashMap<>();
    private final Map<Integer, String> ssrcToUserMap = new ConcurrentHashMap<>();
    private final Map<String, User> userIdToUserCache = new ConcurrentHashMap<>();
    private final AtomicBoolean running = new AtomicBoolean(false);
    private String encryptionMode;
    private WebSocket voiceWebSocket;
    private DatagramSocket udpSocket;
    private InetSocketAddress voiceUdpAddress;
    private int ssrc;
    private volatile byte[] secretKey;
    private GatewayDevice activeGateway;
    private int mappedPort = -1;
    private FluxClient.AudioReceiveHandler receiveHandler;
    private Thread udpListenerThread;

    public VoiceConnectionImpl(String guildId, String userId, FluxClient client, JsonEngine jsonEngine, OkHttpClient httpClient) {
        this.guildId = guildId;
        this.userId = userId;
        this.client = client;
        this.jsonEngine = jsonEngine;
        this.httpClient = httpClient;
    }

    @Override
    public CompletableFuture<Void> connect(String sessionId, String token, String endpoint) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        voiceExecutor.submit(() -> {
            try {
                if (running.get() || voiceWebSocket != null) {
                    disconnect();
                }
                String url = "wss://" + endpoint.replace(":80", "") + "/?v=4";
                Request request = new Request.Builder().url(url).build();
                voiceWebSocket = httpClient.newWebSocket(request, new VoiceGatewayHandler(sessionId, token, future));
            } catch (Exception e) {
                LOGGER.error("Failed to initiate voice WebSocket connection for guild {}: {}", guildId, e.getMessage(), e);
                future.completeExceptionally(e);
            }
        });
        return future;
    }

    @Override
    public CompletableFuture<Void> disconnect() {

        return CompletableFuture.runAsync(() -> {

            running.set(false);

            if (voiceWebSocket != null) {
                voiceWebSocket.close(1000, "Client disconnect");
                voiceWebSocket = null;
            }

            if (udpSocket != null) {
                udpSocket.close();
                udpSocket = null;
            }

            if (udpListenerThread != null) {
                try {
                    LOGGER.debug("Waiting for UDP listener thread to terminate...");
                    udpListenerThread.join(2000);
                    if (udpListenerThread.isAlive()) {
                        LOGGER.warn("UDP listener thread did not terminate in time.");
                    } else {
                        LOGGER.debug("UDP listener thread terminated successfully.");
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    LOGGER.error("Interrupted while waiting for UDP listener thread to stop.");
                }
            }

            if (receiveHandler != null) {
                receiveHandler.onShutdown();
            }

            if (activeGateway != null && mappedPort != -1) {
                try {
                    activeGateway.deletePortMapping(mappedPort, "UDP");
                    LOGGER.info("Successfully removed UPnP port mapping for port {}", mappedPort);
                } catch (Exception e) {
                    LOGGER.error("Failed to remove UPnP port mapping: {}", e.getMessage());
                } finally {
                    activeGateway = null;
                    mappedPort = -1;
                }
            }

            opusDecoders.values().forEach(OpusDecoder::close);
            opusDecoders.clear();
            ssrcToUserMap.clear();
            userIdToUserCache.clear();

            LOGGER.info("Voice connection for guild {} fully disconnected.", guildId);
        }, voiceExecutor).whenComplete((res, err) -> {

            if (!voiceExecutor.isShutdown()) {
                voiceExecutor.shutdown();
            }
        });
    }

    @Override
    public void setReceivingHandler(FluxClient.AudioReceiveHandler handler) {
        this.receiveHandler = handler;
    }

    private CompletableFuture<DiscoveredInfo> startUdpDiscovery() {
        CompletableFuture<DiscoveredInfo> future = new CompletableFuture<>();
        voiceExecutor.submit(() -> {
            try {

                try {
                    GatewayDiscover discover = new GatewayDiscover();
                    discover.discover();
                    activeGateway = discover.getValidGateway();
                    if (activeGateway != null) LOGGER.info("UPnP gateway found: {}", activeGateway.getModelName());
                    else LOGGER.warn("No valid UPnP gateway found. Proceeding without port mapping.");
                } catch (Exception e) {
                    LOGGER.warn("UPnP discovery failed: {}", e.getMessage());
                    activeGateway = null;
                }

                udpSocket = new DatagramSocket();
                int localPort = udpSocket.getLocalPort();
                this.mappedPort = localPort;

                if (activeGateway != null) {
                    try {
                        InetAddress localAddress = activeGateway.getLocalAddress();
                        LOGGER.info("Requesting UPnP port mapping: external {} -> internal {}:{} (UDP)", localPort, localAddress.getHostAddress(), localPort);
                        if (!activeGateway.getSpecificPortMappingEntry(localPort, "UDP", new PortMappingEntry())) {
                            activeGateway.addPortMapping(localPort, localPort, localAddress.getHostAddress(), "UDP", "Flux Voice");
                            LOGGER.info("Successfully created UPnP port mapping for port {}", localPort);
                        } else {
                            LOGGER.warn("UPnP port mapping for {} already exists.", localPort);
                        }
                    } catch (Exception e) {
                        LOGGER.error("An error occurred during UPnP port mapping.", e);
                    }
                }

                ByteBuffer discoveryBuffer = ByteBuffer.allocate(74);
                discoveryBuffer.order(ByteOrder.BIG_ENDIAN);
                discoveryBuffer.putShort(0, (short) 1);
                discoveryBuffer.putShort(2, (short) 70);
                discoveryBuffer.putInt(4, ssrc);

                LOGGER.debug("Sending UDP discovery packet to {}", voiceUdpAddress);
                udpSocket.send(new DatagramPacket(discoveryBuffer.array(), 74, voiceUdpAddress));

                DatagramPacket receivedPacket = new DatagramPacket(new byte[74], 74);
                udpSocket.setSoTimeout(5000);
                udpSocket.receive(receivedPacket);

                ByteBuffer responseBuffer = ByteBuffer.wrap(receivedPacket.getData());

                int nullPos = 8;
                while (nullPos < 72 && responseBuffer.get(nullPos) != 0) {
                    nullPos++;
                }
                if (nullPos == 72) throw new IOException("Invalid IP discovery response: IP not null-terminated.");

                String externalIp = new String(receivedPacket.getData(), 8, nullPos - 8, StandardCharsets.UTF_8);
                int externalPort = Short.toUnsignedInt(responseBuffer.getShort(72));

                future.complete(new DiscoveredInfo(externalIp, externalPort));

            } catch (Exception e) {
                LOGGER.error("Failed during UDP IP Discovery.", e);
                future.completeExceptionally(e);
                if (udpSocket != null) udpSocket.close();
            }
        });
        return future;
    }

    private void startUdpListener() {
        running.set(true);

        this.udpListenerThread = new Thread(this::listenUdp, "Flux-UDP-Listener-" + guildId);
        this.udpListenerThread.setDaemon(true);
        this.udpListenerThread.start();
        LOGGER.info("UDP listener started for guild {}.", guildId);
    }

    private void listenUdp() {
        try {
            if (udpSocket != null && !udpSocket.isClosed()) {
                udpSocket.setSoTimeout(0);
            }
            byte[] buf = new byte[4096];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            while (running.get() && udpSocket != null && !udpSocket.isClosed()) {
                udpSocket.receive(packet);
                if (packet.getLength() > 12) {
                    processAudioPacket(ByteBuffer.wrap(packet.getData(), 0, packet.getLength()));
                }
            }
        } catch (SocketException e) {
            if (running.get()) {
                LOGGER.debug("UDP socket closed for guild {}, listener thread exiting.", guildId);
            }
        } catch (Exception e) {
            if (running.get()) LOGGER.error("Error in UDP listener for guild {}", guildId, e);
        }
        LOGGER.debug("UDP listener loop for guild {} has finished.", guildId);
    }

    private void processAudioPacket(ByteBuffer rtpPacket) {
        try {
            if (!secretKeyLatch.await(2, TimeUnit.SECONDS)) {
                LOGGER.warn("Timed out waiting for secret key. Dropping audio packet.");
                return;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }

        int ssrc = rtpPacket.getInt(8);
        String userId = ssrcToUserMap.get(ssrc);

        if (userId == null || userId.equals(this.userId) || receiveHandler == null) {
            return;
        }

        User user = userIdToUserCache.computeIfAbsent(userId, id -> {
            try {
                return client.getUserById(id).join();
            } catch (Exception e) {
                LOGGER.error("Failed to fetch user object for ID {}", id, e);
                return null;
            }
        });

        if (user == null || !receiveHandler.canReceiveUser(user)) {
            return;
        }

        rtpPacket.position(0);
        int packetLimit = rtpPacket.limit();

        byte[] opusAudio;

        if (encryptionMode != null && encryptionMode.startsWith("aead_")) {
            opusAudio = decryptAEAD(rtpPacket, packetLimit);
        } else {
            opusAudio = decryptLegacy(rtpPacket, packetLimit);
        }

        if (opusAudio == null) {
            return;
        }

        if (opusAudio.length <= 12) {
            LOGGER.warn("Decrypted packet for SSRC {} is too small to contain Opus data (length: {}).", ssrc, opusAudio.length);
            return;
        }

        byte[] rawOpusData = java.util.Arrays.copyOfRange(opusAudio, 12, opusAudio.length);

        if (rawOpusData.length == 0) {
            return;
        }

        OpusDecoder decoder = opusDecoders.computeIfAbsent(ssrc, k -> new OpusDecoder());
        byte[] pcmAudio = decoder.decode(rawOpusData);

        if (pcmAudio == null) {
            return;
        }

        receiveHandler.handleUserAudio(user, pcmAudio);
    }

    private byte[] decryptAEAD(ByteBuffer rtpPacket, int packetLimit) {
        try {
            if (packetLimit < 16) return null;

            int nonceValue = rtpPacket.getInt(packetLimit - 4);
            byte[] nonce = new byte[12];
            ByteBuffer.wrap(nonce).order(ByteOrder.BIG_ENDIAN).putInt(8, nonceValue);

            int encryptedAudioLength = packetLimit - 12 - 4;
            if (encryptedAudioLength <= 0) return null;

            byte[] encryptedAudio = new byte[encryptedAudioLength];
            rtpPacket.position(12);
            rtpPacket.get(encryptedAudio, 0, encryptedAudioLength);

            if (encryptionMode.contains("aes256_gcm")) {
                javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("AES/GCM/NoPadding");
                javax.crypto.spec.SecretKeySpec keySpec = new javax.crypto.spec.SecretKeySpec(this.secretKey, "AES");
                javax.crypto.spec.GCMParameterSpec gcmSpec = new javax.crypto.spec.GCMParameterSpec(128, nonce);
                cipher.init(javax.crypto.Cipher.DECRYPT_MODE, keySpec, gcmSpec);

                byte[] rtpHeader = new byte[12];
                rtpPacket.position(0);
                rtpPacket.get(rtpHeader, 0, 12);
                cipher.updateAAD(rtpHeader);

                return cipher.doFinal(encryptedAudio);

            } else if (encryptionMode.contains("xchacha20") || encryptionMode.contains("chacha20")) {
                TweetNaclFast.SecretBox cryptoBox = new TweetNaclFast.SecretBox(this.secretKey);
                return cryptoBox.open(encryptedAudio, nonce);

            } else {
                LOGGER.warn("Unknown AEAD encryption mode: {}", encryptionMode);
                return null;
            }

        } catch (Exception e) {
            LOGGER.warn("Failed to decrypt AEAD packet using mode '{}': {}", encryptionMode, e.getMessage());
            return null;
        }
    }

    private byte[] decryptLegacy(ByteBuffer rtpPacket, int packetLimit) {
        final byte[] nonce = new byte[24];
        int encryptedAudioStart;
        int encryptedAudioLength;

        if ("xsalsa20_poly1305_suffix".equals(encryptionMode)) {
            if (packetLimit < 36) return null;
            rtpPacket.get(packetLimit - 24, nonce, 0, 24);
            encryptedAudioStart = 12;
            encryptedAudioLength = packetLimit - 12 - 24;

        } else if ("xsalsa20_poly1305_lite".equals(encryptionMode)) {
            if (packetLimit < 12) return null;
            rtpPacket.position(0);
            rtpPacket.get(nonce, 0, 12);
            encryptedAudioStart = 12;
            encryptedAudioLength = packetLimit - 12;

        } else {
            LOGGER.warn("Unknown legacy encryption mode '{}'", encryptionMode);
            return null;
        }

        if (encryptedAudioLength <= 0) return null;

        final byte[] encryptedAudio = new byte[encryptedAudioLength];
        rtpPacket.position(encryptedAudioStart);
        rtpPacket.get(encryptedAudio, 0, encryptedAudioLength);

        TweetNaclFast.SecretBox cryptoBox = new TweetNaclFast.SecretBox(this.secretKey);
        byte[] opusAudio = cryptoBox.open(encryptedAudio, nonce);

        if (opusAudio == null) {
            LOGGER.warn("Failed to decrypt legacy packet using mode '{}'", encryptionMode);
        }

        return opusAudio;
    }

    public String getEncryptionMode() {
        return encryptionMode;
    }

    public void setEncryptionMode(String encryptionMode) {
        this.encryptionMode = encryptionMode;
    }

    private record HeartbeatPayload(int op, long d) {
    }

    private record IdentifyPayload(int op, IdentifyData d) {
    }

    private record IdentifyData(
            @JsonProperty("server_id") String serverId,
            @JsonProperty("user_id") String userId,
            @JsonProperty("session_id") String sessionId,
            String token
    ) {
    }

    private record SelectProtocolPayload(int op, SelectProtocolData d) {
    }

    private record SelectProtocolData(
            String protocol,
            SelectProtocolConnectionData data
    ) {
    }

    private record SelectProtocolConnectionData(
            String address,
            int port,
            String mode
    ) {
    }

    private record DiscoveredInfo(String ip, int port) {
    }

    private class VoiceGatewayHandler extends WebSocketListener {
        private final String sessionId;
        private final String token;
        private final CompletableFuture<Void> connectionFuture;
        private final ScheduledExecutorService heartbeatExecutor = Executors.newSingleThreadScheduledExecutor(r -> new Thread(r, "Flux-Voice-Heartbeat"));

        public VoiceGatewayHandler(String sessionId, String token, CompletableFuture<Void> future) {
            this.sessionId = sessionId;
            this.token = token;
            this.connectionFuture = future;
        }

        @Override
        public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
            LOGGER.info("Voice WebSocket opened for guild {}. Sending Identify.", guildId);
            var payload = new IdentifyPayload(0, new IdentifyData(guildId, userId, sessionId, token));
            webSocket.send(jsonEngine.toJsonString(payload));
            response.close();
        }

        @Override
        public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {

            LOGGER.warn("RAW VOICE GATEWAY PAYLOAD RECEIVED: {}", text);

            JsonNode payload = jsonEngine.fromJsonString(text, JsonNode.class);
            int op = payload.get("op").asInt();
            JsonNode data = payload.get("d");

            switch (op) {
                case 2:
                    LOGGER.info("Voice READY received (Opcode 2). SSRC: {}, Port: {}. Starting UDP discovery.", data.get("ssrc").asInt(), data.get("port").asInt());
                    ssrc = data.get("ssrc").asInt();
                    voiceUdpAddress = new InetSocketAddress(data.get("ip").asText(), data.get("port").asInt());

                    JsonNode modesNode = data.get("modes");
                    String selectedMode = "xsalsa20_poly1305_suffix";

                    if (modesNode != null && modesNode.isArray() && !modesNode.isEmpty()) {
                        selectedMode = modesNode.get(0).asText();
                        LOGGER.info("Selected encryption mode: {} (from {} available modes)", selectedMode, modesNode.size());
                    } else {
                        LOGGER.warn("No encryption modes in READY payload, using fallback: {}", selectedMode);
                    }

                    final String encryptionMode = selectedMode;

                    JsonNode ssrcsNode = data.get("ssrcs");
                    if (ssrcsNode != null && ssrcsNode.isArray() && !ssrcsNode.isEmpty()) {
                        LOGGER.info("Found 'ssrcs' array in READY payload. Populating SSRC map...");
                        for (JsonNode ssrcMapping : ssrcsNode) {
                            if (ssrcMapping.has("user_id") && ssrcMapping.has("ssrc")) {
                                String ssrcUserId = ssrcMapping.get("user_id").asText();
                                int userSsrc = ssrcMapping.get("ssrc").asInt();
                                ssrcToUserMap.put(userSsrc, ssrcUserId);
                                LOGGER.info("  -> Mapped SSRC {} to User ID {} from READY payload.", userSsrc, ssrcUserId);
                            }
                        }
                    } else {
                        LOGGER.warn("Voice READY payload did not contain a valid 'ssrcs' array. Map will be populated by SPEAKING events only.");
                    }

                    startUdpDiscovery().thenAccept(info -> {
                        LOGGER.info("UDP Discovery successful. External IP: {}, Port: {}. Sending Select Protocol.", info.ip, info.port);

                        var connData = new SelectProtocolConnectionData(info.ip, info.port, encryptionMode);

                        var selectPayload = new SelectProtocolPayload(1, new SelectProtocolData("udp", connData));
                        webSocket.send(jsonEngine.toJsonString(selectPayload));
                    }).exceptionally(e -> {
                        LOGGER.error("UDP IP Discovery failed, aborting connection.", e);
                        connectionFuture.completeExceptionally(e);
                        disconnect();
                        return null;
                    });
                    break;

                case 4:
                    LOGGER.info("Voice Session Description received (Opcode 4). Storing secret key.");
                    try {
                        JsonNode keyNode = data.get("secret_key");
                        if (keyNode == null || !keyNode.isArray()) {
                            throw new IOException("secret_key is missing or not a JSON array in Session Description payload.");
                        }

                        byte[] keyBytes = new byte[keyNode.size()];
                        for (int i = 0; i < keyNode.size(); i++) {
                            keyBytes[i] = (byte) keyNode.get(i).asInt();
                        }
                        VoiceConnectionImpl.this.secretKey = keyBytes;
                        VoiceConnectionImpl.this.encryptionMode = data.get("mode").asText();

                        VoiceConnectionImpl.this.secretKeyLatch.countDown();
                        voiceExecutor.submit(VoiceConnectionImpl.this::startUdpListener);
                        connectionFuture.complete(null);

                    } catch (IOException e) {
                        connectionFuture.completeExceptionally(new RuntimeException("Failed to parse secret_key", e));
                        disconnect();
                    }
                    break;

                case 5:
                    String speakingUserId = data.get("user_id").asText();
                    int speakingSsrc = data.get("ssrc").asInt();
                    boolean speaking = data.has("speaking") && data.get("speaking").asBoolean();

                    ssrcToUserMap.put(speakingSsrc, speakingUserId);

                    LOGGER.info("Speaking Update received (Opcode 5): User {} (SSRC {}) has updated speaking state (Speaking={}). Map updated.", speakingUserId, speakingSsrc, speaking);
                    break;

                case 8:
                    LOGGER.info("Voice Hello received (Opcode 8). Starting heartbeat.");
                    int interval = data.get("heartbeat_interval").asInt();
                    heartbeatExecutor.scheduleAtFixedRate(() -> {
                        var heartbeat = new HeartbeatPayload(3, System.currentTimeMillis());
                        webSocket.send(jsonEngine.toJsonString(heartbeat));
                    }, 0, interval, TimeUnit.MILLISECONDS);
                    break;

                case 6:
                    LOGGER.trace("Voice Heartbeat ACK received (Opcode 6).");
                    break;

                default:
                    LOGGER.warn("Received unhandled voice opcode: {}", op);
                    break;
            }
        }

        @Override
        public void onClosing(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
            LOGGER.info("Voice WebSocket closing for guild {}. Code: {}, Reason: {}", guildId, code, reason);
            heartbeatExecutor.shutdownNow();
        }

        @Override
        public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, @Nullable Response response) {
            LOGGER.error("Voice WebSocket failed for guild {}", guildId, t);
            if (response != null) {
                response.close();
            }
            if (!connectionFuture.isDone()) {
                connectionFuture.completeExceptionally(t);
            }
            heartbeatExecutor.shutdownNow();
            disconnect();
        }
    }
}
