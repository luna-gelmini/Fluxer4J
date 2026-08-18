package flux.api.payload.moderation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TimeoutPayload {
    @JsonProperty("communication_disabled_until")
    private final String communicationDisabledUntil;

    public TimeoutPayload(String communicationDisabledUntil) {
        this.communicationDisabledUntil = communicationDisabledUntil;
    }
}
