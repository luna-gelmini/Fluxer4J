package flux.model.gateway;

import flux.api.FluxClient;
import flux.api.entities.Message;
import flux.api.entities.User;
import flux.api.event.AbstractEvent;

public class MessageCreateEvent extends AbstractEvent {
    private final Message message;

    public MessageCreateEvent(FluxClient fluxClient, Message message) {
        super(fluxClient);
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }

    public User getAuthor() {
        return message.getAuthor();
    }

    public String getContentRaw() {
        return message.getContentRaw();
    }

    public String getChannelId() {
        return message.getChannelId();
    }

}
