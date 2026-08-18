package flux.model.role;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import flux.api.FluxClient;
import flux.api.entities.Guild;
import flux.api.entities.Role;
import flux.model.AbstractFluxerEntity;

import java.awt.*;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class RoleImpl extends AbstractFluxerEntity implements Role {

    @JsonProperty("name")
    private String name;

    @JsonProperty("color")
    private int rgbColorValue;

    @JsonProperty("hoist")
    private boolean hoisted;

    @JsonProperty("position")
    private int position;

    @JsonProperty("permissions")
    private String permissionsString;

    @JsonProperty("managed")
    private boolean managed;

    @JsonProperty("mentionable")
    private boolean mentionable;

    @JsonIgnore
    private String guildId;

    public RoleImpl() {
    }

    public void setGuildId(String guildId) {
        this.guildId = guildId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Color getColor() {
        return rgbColorValue == 0 ? null : new Color(rgbColorValue);
    }

    @Override
    public int getColorRaw() {
        return rgbColorValue;
    }

    @Override
    public boolean isHoisted() {
        return hoisted;
    }

    @Override
    public int getPosition() {
        return position;
    }

    @Override
    public long getPermissionsRaw() {
        try {
            return Long.parseLong(permissionsString);
        } catch (NumberFormatException e) {
            System.err.println("Aviso: Falha ao converter string de permissões para long: " + permissionsString);
            return 0L;
        }
    }

    @Override
    public boolean isManaged() {
        return managed;
    }

    @Override
    public boolean isMentionable() {
        return mentionable;
    }

    @Override
    public String getAsMention() {
        return "<@&" + getId() + ">";
    }

    @Override
    public String getGuildId() {
        return guildId;
    }

    @Override
    public CompletableFuture<Guild> retrieveGuild(FluxClient client) {
        if (guildId == null || client == null) {
            return CompletableFuture.failedFuture(new IllegalStateException("GuildId ou FluxClient não disponível."));
        }
        return client.getGuildById(guildId);
    }

    @Override
    public int compareTo(Role other) {

        if (this.getPosition() != other.getPosition()) {
            return Integer.compare(other.getPosition(), this.getPosition());
        }
        return this.getId().compareTo(other.getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoleImpl role = (RoleImpl) o;
        return Objects.equals(id, role.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "RoleImpl{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", position=" + position +
                (guildId != null ? ", guildId='" + guildId + '\'' : "") +
                '}';
    }
}
