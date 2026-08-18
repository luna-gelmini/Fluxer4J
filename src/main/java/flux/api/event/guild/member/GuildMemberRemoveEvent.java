package flux.api.event.guild.member;

import flux.api.FluxClient;
import flux.api.entities.Guild;
import flux.api.entities.User;
import flux.api.event.AbstractEvent;

import java.util.concurrent.CompletableFuture;

public class GuildMemberRemoveEvent extends AbstractEvent {
    private final String guildId;
    private final User user;

    public GuildMemberRemoveEvent(FluxClient fluxClient, String guildId, User user) {
        super(fluxClient);
        this.guildId = guildId;
        this.user = user;
    }

    public String getGuildId() {
        return guildId;
    }

    public User getUser() {
        return user;
    }

    public CompletableFuture<Guild> getGuild() {
        return fluxClient.getGuildById(guildId);
    }
}
