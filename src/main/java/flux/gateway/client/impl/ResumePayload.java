package flux.gateway.client.impl;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResumePayload {
    @JsonProperty("token")
    public final String token;

    @JsonProperty("session_id")
    public final String sessionId;

    @JsonProperty("seq")
    public final int sequence;

    public ResumePayload(String token, String sessionId, int sequence) {
        this.token = token;
        this.sessionId = sessionId;
        this.sequence = sequence;
    }
}
