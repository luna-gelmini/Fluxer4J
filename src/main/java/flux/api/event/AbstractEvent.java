package flux.api.event;

import flux.api.FluxClient;

public abstract class AbstractEvent implements Event {
    protected final FluxClient fluxClient;

    public AbstractEvent(FluxClient fluxClient) {
        this.fluxClient = fluxClient;
    }

    @Override
    public FluxClient getFluxClient() {
        return fluxClient;
    }
}
