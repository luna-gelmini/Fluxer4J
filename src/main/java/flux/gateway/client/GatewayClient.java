package flux.gateway.client;

import flux.api.gateway.GatewayIntent;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public interface GatewayClient {

    void setBotToken(String botToken);

    void setGatewayUrl(String gatewayUrl);

    void setIntents(Collection<GatewayIntent> intents);

    void sendVoiceStateUpdate(String guildId, @Nullable String channelId, boolean selfMute, boolean selfDeaf);

    CompletableFuture<Void> connect();

    void disconnect();

    void send(String jsonPayload);

    void sendPresenceUpdate(Object payloadData);

    void playSoundboardSound(String guildId, String channelId, String soundId);

}
