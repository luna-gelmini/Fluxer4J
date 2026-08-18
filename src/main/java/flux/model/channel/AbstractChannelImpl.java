package flux.model.channel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import flux.api.FluxClient;
import flux.api.entities.channel.Channel;
import flux.api.entities.channel.ChannelType;
import flux.model.AbstractFluxerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public abstract class AbstractChannelImpl extends AbstractFluxerEntity implements Channel {

    @JsonProperty("name")
    protected String name;

    @JsonProperty("type")
    protected int typeId;

    @JsonProperty("guild_id")
    protected String guildId;

    @JsonIgnore
    protected FluxClient fluxClient;

    @Override
    public String getName() { return name; }

    @Override
    public ChannelType getType() { return ChannelType.fromId(typeId); }

    public String getGuildId() { return guildId; }

    @Override
    public void setFluxClient(FluxClient client) {
        this.fluxClient = Objects.requireNonNull(client, "FluxClient cannot be null for ChannelImpl.");
    }
}
