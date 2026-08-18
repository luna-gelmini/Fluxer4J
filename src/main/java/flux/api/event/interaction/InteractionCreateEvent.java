package flux.api.event.interaction;

import flux.api.FluxClient;
import flux.api.entities.interaction.Interaction;
import flux.api.event.AbstractEvent;

public class InteractionCreateEvent extends AbstractEvent {

    private final Interaction interaction;

    public InteractionCreateEvent(FluxClient client, Interaction interaction) {
        super(client);
        this.interaction = interaction;
    }

    public Interaction getInteraction() {
        return interaction;
    }
}
