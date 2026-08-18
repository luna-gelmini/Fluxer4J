package flux.model.channel;

import com.fasterxml.jackson.annotation.JsonProperty;
import flux.api.entities.channel.ChannelType;
import flux.api.entities.channel.VoiceChannel;

public class VoiceChannelImpl extends AbstractChannelImpl implements VoiceChannel {

    @JsonProperty("user_limit")
    private Integer userLimit;

    @JsonProperty("bitrate")
    private Integer bitrate;

    @JsonProperty("rtc_region")
    private String rtcRegion;

    @JsonProperty("status")
    private String status;

    @JsonProperty("parentId")
    private String parentId;

    public VoiceChannelImpl() {}

    @Override
    public Integer getUserLimit() {
        return userLimit;
    }

    @Override
    public Integer getBitrate() {
        return bitrate;
    }

    @Override
    public String getRtcRegion() {
        return rtcRegion;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public ChannelType getType() {
        return ChannelType.fromId(typeId);
    }

    @Override
    public String getParentId() { return parentId; }

    @Override
    public String toString() {
        return "VoiceChannelImpl{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type=" + getType() +
                ", userLimit=" + userLimit +
                ", parentId='" + parentId + '\'' +
                '}';
    }
}
