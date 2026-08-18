package flux.model.gateway;

import flux.api.FluxClient;
import flux.api.entities.User;
import flux.api.event.AbstractEvent;

public class ReadyEvent extends AbstractEvent {
    private final User selfUser;
    private final String sessionId;
    private final String resumeGatewayUrl;
    private final int gatewayVersion;

    public ReadyEvent(FluxClient fluxClient, User selfUser, String sessionId, String resumeGatewayUrl, int gatewayVersion ) {
        super(fluxClient);
        this.selfUser = selfUser;
        this.sessionId = sessionId;
        this.resumeGatewayUrl = resumeGatewayUrl;
        this.gatewayVersion = gatewayVersion;

    }

    public User getSelfUser() {
        return selfUser;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getResumeGatewayUrl() {
        return resumeGatewayUrl;
    }

    public int getGatewayVersion() {
        return gatewayVersion;
    }

}
