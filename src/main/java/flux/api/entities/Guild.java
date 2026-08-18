package flux.api.entities;

import flux.api.FluxClient;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface Guild extends FluxerEntity {

    String getName();

    String getIconId();

    String getIconUrl();

    String getOwnerId();

    CompletableFuture<User> retrieveOwner(FluxClient client);

    List<Role> getRoles();

    CompletableFuture<List<Role>> retrieveRoles(FluxClient client);

    List<VoiceState> getVoiceStates();

}
