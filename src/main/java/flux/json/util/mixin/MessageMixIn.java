package flux.json.util.mixin;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import flux.model.message.MessageImpl;

@JsonDeserialize(as = MessageImpl.class)
public abstract class MessageMixIn {
}
