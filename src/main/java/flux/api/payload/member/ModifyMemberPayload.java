package flux.api.payload.member;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ModifyMemberPayload {

    @JsonProperty("channel_id")
    private String channelId;

    public ModifyMemberPayload() {
    }

    public ModifyMemberPayload(String voiceChannelId) {
        this.channelId = voiceChannelId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }
}
