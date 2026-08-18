package flux.api.payload.moderation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BanPayload {
    @JsonProperty("delete_message_days")
    private final Integer deleteMessageDays;

    @JsonProperty("reason")
    private final String reason;

    public BanPayload(int deleteMessageSeconds, String reason) {
        this.deleteMessageDays = deleteMessageSeconds <= 0 ? 0 : (deleteMessageSeconds + 86399) / 86400;
        this.reason = reason;
    }
}
