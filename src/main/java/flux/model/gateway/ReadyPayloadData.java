package flux.model.gateway;

import com.fasterxml.jackson.annotation.JsonProperty;
import flux.model.user.UserImpl;

import java.util.List;

public class ReadyPayloadData {

    @JsonProperty("v")
    private int gatewayVersion;

    @JsonProperty("user")
    private UserImpl selfUser;

    @JsonProperty("guilds")
    private List<UnavailableGuild> guilds;

    @JsonProperty("session_id")
    private String sessionId;

    @JsonProperty("resume_gateway_url")
    private String resumeGatewayUrl;

    public int getGatewayVersion() {
        return gatewayVersion;
    }

    public UserImpl getSelfUser() {
        return selfUser;
    }

    public List<UnavailableGuild> getGuilds() {
        return guilds;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getResumeGatewayUrl() {
        return resumeGatewayUrl;
    }

    public static class UnavailableGuild {
        @JsonProperty("id")
        private String id;
        @JsonProperty("unavailable")
        private boolean unavailable;

        public String getId() {
            return id;
        }

        public boolean isUnavailable() {
            return unavailable;
        }
    }
}
