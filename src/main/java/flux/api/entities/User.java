package flux.api.entities;

public interface User extends FluxerEntity {

    String getUsername();

    String getDiscriminator();

    String getGlobalName();

    String getAsTag();

    String getAvatarId();

    String getEffectiveAvatarUrl();

    boolean isBot();

    boolean isSystem();

}
