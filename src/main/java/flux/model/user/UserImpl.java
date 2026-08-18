package flux.model.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import flux.api.ApiEnvironment;
import flux.api.entities.User;
import flux.model.AbstractFluxerEntity;

public class UserImpl extends AbstractFluxerEntity implements User {

    @JsonProperty("username")
    private String username;

    @JsonProperty("global_name")
    private String global_name;

    @JsonProperty("discriminator")
    private String discriminator;

    @JsonProperty("avatar")
    private String avatarId;

    @JsonProperty("bot")
    private boolean bot;

    @JsonProperty("system")
    private boolean system;

    public UserImpl() {
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getDiscriminator() {
        return discriminator;
    }

    @Override
    public String getGlobalName() {
        return global_name;
    }

    @Override
    public String getAsTag() {

        return "0".equals(discriminator) ? username : username + "#" + discriminator;
    }

    @Override
    public String getAvatarId() {
        return avatarId;
    }

    @Override
    public String getEffectiveAvatarUrl() {
        if (avatarId != null) {
            return ApiEnvironment.avatarUrl(getId(), avatarId);
        }

        int defaultAvatarIndex;
        if ("0".equals(discriminator)) {
            defaultAvatarIndex = (int) ((getIdLong() >> 22) % 6);
        } else {
            defaultAvatarIndex = Integer.parseInt(discriminator) % 5;
        }
        return ApiEnvironment.defaultAvatarUrl(defaultAvatarIndex);
    }

    @Override
    public boolean isBot() {
        return bot;
    }

    @Override
    public boolean isSystem() {
        return system;
    }

    @Override
    public String toString() {
        return "UserImpl{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", global_name='" + global_name + '\'' +
                ", discriminator='" + discriminator + '\'' +
                ", bot=" + bot +
                '}';
    }
}
