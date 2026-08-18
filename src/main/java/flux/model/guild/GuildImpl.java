package flux.model.guild;

import com.fasterxml.jackson.annotation.JsonProperty;
import flux.api.ApiEnvironment;
import flux.api.FluxClient;
import flux.api.entities.Guild;
import flux.api.entities.Role;
import flux.api.entities.User;
import flux.api.entities.VoiceState;
import flux.model.AbstractFluxerEntity;
import flux.model.role.RoleImpl;
import flux.model.voice.VoiceStateImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class GuildImpl extends AbstractFluxerEntity implements Guild {

    @JsonProperty("name")
    private String name;

    @JsonProperty("icon")
    private String iconId;

    @JsonProperty("owner_id")
    private String ownerId;

    @JsonProperty("roles")
    private List<RoleImpl> roles = new ArrayList<>();

    @JsonProperty("voice_states")
    private List<VoiceStateImpl> voiceStates = new ArrayList<>();

    public GuildImpl() {
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getIconId() {
        return iconId;
    }

    @Override
    public String getIconUrl() {
        if (iconId == null) {
            return null;
        }
        return ApiEnvironment.guildIconUrl(getId(), iconId);
    }

    @Override
    public String getOwnerId() {
        return ownerId;
    }

    @Override
    public CompletableFuture<User> retrieveOwner(FluxClient client) {
        if (ownerId == null || client == null) {
            return CompletableFuture.failedFuture(new IllegalStateException("OwnerId ou FluxClient não disponível."));
        }
        return client.getUserById(ownerId);
    }

    @Override
    public List<Role> getRoles() {

        return Collections.unmodifiableList(new ArrayList<>(roles));
    }

    @Override
    public CompletableFuture<List<Role>> retrieveRoles(FluxClient client) {
        if (client == null) {
            return CompletableFuture.failedFuture(new IllegalStateException("FluxClient não disponível."));
        }

        return client.getGuildRoles(getId())
                .thenApply(retrievedRoles -> retrievedRoles);
    }

    @Override
    public String toString() {
        return "GuildImpl{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", ownerId='" + ownerId + '\'' +
                ", rolesCount=" + (roles != null ? roles.size() : 0) +
                '}';
    }

    @Override
    public List<VoiceState> getVoiceStates() {
        return Collections.unmodifiableList(new ArrayList<>(voiceStates));
    }
}
