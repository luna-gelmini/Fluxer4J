package flux.api.entities;

import flux.api.FluxClient;
import flux.api.payload.permission.Permission;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface Member extends FluxerEntity {

    CompletableFuture<Long> getPermissionsRaw();

    CompletableFuture<Boolean> hasPermission(Permission permission);

    CompletableFuture<Boolean> hasPermissions(Collection<Permission> permissions);
    User getUser();

    String getNickname();

    String getEffectiveName();

    List<String> getRoleIds();

    CompletableFuture<List<Role>> getRoles(FluxClient client);

    OffsetDateTime getTimeJoined();

    OffsetDateTime getTimeBoosted();

    boolean isDeafened();

    boolean isMuted();

    String getGuildId();

    CompletableFuture<Guild> retrieveGuild(FluxClient client);

}
