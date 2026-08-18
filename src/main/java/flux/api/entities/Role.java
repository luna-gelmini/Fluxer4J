package flux.api.entities;

import flux.api.FluxClient;

import java.awt.*;
import java.util.concurrent.CompletableFuture;

public interface Role extends FluxerEntity, Comparable<Role> {

    String getName();

    Color getColor();

    int getColorRaw();

    boolean isHoisted();

    int getPosition();

    long getPermissionsRaw();

    boolean isManaged();

    boolean isMentionable();

    String getAsMention();

    String getGuildId();

    CompletableFuture<Guild> retrieveGuild(FluxClient client);

}
