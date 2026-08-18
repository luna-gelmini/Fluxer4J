package flux.api.event.channel;

import flux.api.FluxClient;
import flux.api.entities.channel.Channel;
import flux.api.event.AbstractEvent;

public class ChannelUpdateEvent extends AbstractEvent {
    private final Channel channel;
    private final String guildId;

    public ChannelUpdateEvent(FluxClient client, Channel channel, String guildId) {
        super(client);
        this.channel = channel;
        this.guildId = guildId;
    }

    public Channel getChannel() {
        return channel;
    }

    public String getChannelId() {
        return channel != null ? channel.getId() : null;
    }

    public String getChannelName() {
        return channel != null ? channel.getName() : null;
    }

    public String getGuildId() {
        return guildId;
    }
}
