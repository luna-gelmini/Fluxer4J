package flux.api.event.voice;

import flux.api.FluxClient;
import flux.api.event.AbstractEvent;

public class VoiceServerUpdateEvent extends AbstractEvent {
    private final String guildId;
    private final String token;
    private final String endpoint;

    public VoiceServerUpdateEvent(FluxClient fluxClient, String guildId, String token, String endpoint) {
        super(fluxClient);
        this.guildId = guildId;
        this.token = token;
        this.endpoint = endpoint;
    }

    public String getGuildId() {
        return guildId;
    }

    public String getToken() {
        return token;
    }

    public String getEndpoint() {
        return endpoint;
    }
}
