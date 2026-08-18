package flux.api.payload.member;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ModifyMemberPayload {

    @JsonProperty("channel_id")
    private String channelId;

    @JsonProperty("connection_id")
    private String connectionId;

    public ModifyMemberPayload() {
    }

    public ModifyMemberPayload(String voiceChannelId) {
        this.channelId = voiceChannelId;
    }

    public ModifyMemberPayload(String voiceChannelId, String connectionId) {
        this.channelId = voiceChannelId;
        this.connectionId = connectionId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(String connectionId) {
        this.connectionId = connectionId;
    }
}
