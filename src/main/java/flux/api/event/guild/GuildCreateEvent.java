package flux.api.event.guild;

import flux.api.FluxClient;
import flux.api.entities.Guild;
import flux.api.event.AbstractEvent;

public class GuildCreateEvent extends AbstractEvent {
    private final Guild guild;

    public GuildCreateEvent(FluxClient fluxClient, Guild guild) {
        super(fluxClient);
        this.guild = guild;
    }

    public Guild getGuild() {
        return guild;
    }
}
