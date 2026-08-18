package flux.api.voice;

import flux.api.FluxClient;

import java.util.concurrent.CompletableFuture;

public interface VoiceConnection {
    CompletableFuture<Void> connect(String sessionId, String token, String endpoint);

    CompletableFuture<Void> disconnect();

    void setReceivingHandler(FluxClient.AudioReceiveHandler handler);
}
