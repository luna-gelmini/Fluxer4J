package flux.model.channel;

import com.fasterxml.jackson.annotation.JsonProperty;
import flux.api.entities.channel.TextChannel;

public class TextChannelImpl extends AbstractChannelImpl implements TextChannel {

    @JsonProperty("topic")
    private String topic;

    @JsonProperty("nsfw")
    private boolean nsfw;

    public TextChannelImpl() {
    }

    @Override
    public String getTopic() {
        return topic;
    }

    @Override
    public boolean isNsfw() {
        return nsfw;
    }

    @Override
    public String toString() {
        return "TextChannelImpl{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type=" + getType() +
                ", topic='" + topic + '\'' +
                ", nsfw=" + nsfw +
                '}';
    }
}
