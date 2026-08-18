package flux.api.payload.channel;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChannelModifyPayload {

    @JsonProperty("name")
    private String name;

    @JsonProperty("topic")
    private String topic;

    @JsonProperty("nsfw")
    private Boolean nsfw;

    @JsonProperty("parent_id")
    private String parentId;

    @JsonProperty("user_limit")
    private Integer userLimit;

    @JsonProperty("bitrate")
    private Integer bitrate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getUserLimit() {
        return userLimit;
    }

    public void setUserLimit(Integer userLimit) {
        this.userLimit = userLimit;
    }

}
