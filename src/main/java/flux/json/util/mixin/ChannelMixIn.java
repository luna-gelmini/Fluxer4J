package flux.json.util.mixin;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import flux.model.channel.CategoryChannelImpl;
import flux.model.channel.TextChannelImpl;
import flux.model.channel.VoiceChannelImpl;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = TextChannelImpl.class, name = "0"),
        @JsonSubTypes.Type(value = VoiceChannelImpl.class, name = "2"),
        @JsonSubTypes.Type(value = CategoryChannelImpl.class, name = "4"),
        @JsonSubTypes.Type(value = TextChannelImpl.class, name = "5"),
        @JsonSubTypes.Type(value = TextChannelImpl.class, name = "10"),
        @JsonSubTypes.Type(value = TextChannelImpl.class, name = "11"),
        @JsonSubTypes.Type(value = TextChannelImpl.class, name = "12"),
        @JsonSubTypes.Type(value = VoiceChannelImpl.class, name = "13"),
        @JsonSubTypes.Type(value = CategoryChannelImpl.class, name = "14"),
        @JsonSubTypes.Type(value = TextChannelImpl.class, name = "15")
})
public abstract class ChannelMixIn {}
