package flux.api.event.message;

import flux.api.FluxClient;
import flux.api.entities.Message;
import flux.api.event.AbstractEvent;

public class MessageUpdateEvent extends AbstractEvent {
    private final Message message;

    public MessageUpdateEvent(FluxClient fluxClient, Message message) {
        super(fluxClient);
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }

    public String getChannelId() {
        return message.getChannelId();
    }
}
