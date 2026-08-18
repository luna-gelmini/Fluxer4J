package flux.api.event.message;

import flux.api.FluxClient;
import flux.api.event.AbstractEvent;

public class MessageDeleteEvent extends AbstractEvent {
    private final String messageId;
    private final String channelId;
    private final String guildId;

    public MessageDeleteEvent(FluxClient fluxClient, String messageId, String channelId, String guildId) {
        super(fluxClient);
        this.messageId = messageId;
        this.channelId = channelId;
        this.guildId = guildId;
    }

    public String getMessageId() {
        return messageId;
    }

    public String getChannelId() {
        return channelId;
    }

    public String getGuildId() {
        return guildId;
    }
}
