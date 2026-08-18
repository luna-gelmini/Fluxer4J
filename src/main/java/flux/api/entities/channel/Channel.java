package flux.api.entities.channel;

import flux.api.FluxClient;
import flux.api.entities.FluxerEntity;

public interface Channel extends FluxerEntity {

    String getName();

    void setFluxClient(FluxClient client);

    ChannelType getType();

    String getGuildId();

}
