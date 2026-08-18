package flux.api.payload.channel;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import flux.api.entities.channel.ChannelType;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateGuildChannelPayload {
    @JsonProperty("name")
    private String name;

    @JsonProperty("type")
    private int type;

    @JsonProperty("topic")
    private String topic;

    @JsonProperty("parent_id")
    private String parentId;

    @JsonProperty("user_limit")
    private Integer user_limit;

    public CreateGuildChannelPayload(String name, ChannelType type) {
        this.name = name;
        this.type = type.getId();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(ChannelType type) {
        this.type = type.getId();
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public void setUserLimit(Integer user_limit) {
        this.user_limit = user_limit;
    }
}
