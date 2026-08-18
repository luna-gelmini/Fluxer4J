package flux.api.event.guild.member;

import flux.api.FluxClient;
import flux.api.entities.Guild;
import flux.api.entities.Member;
import flux.api.event.AbstractEvent;

import java.util.concurrent.CompletableFuture;

public class GuildMemberUpdateEvent extends AbstractEvent {
    private final Member member;

    public GuildMemberUpdateEvent(FluxClient fluxClient, Member member) {
        super(fluxClient);
        this.member = member;
    }

    public Member getMember() {
        return member;
    }

    public String getGuildId() {
        return member.getGuildId();
    }

    public CompletableFuture<Guild> getGuild() {
        return member.retrieveGuild(getFluxClient());
    }
}
