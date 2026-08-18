package flux.api.payload.permission;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PermissionOverwritePayload {
    @JsonProperty("id")
    private String id;

    @JsonProperty("type")
    private int type;

    @JsonProperty("allow")
    private String allow;

    @JsonProperty("deny")
    private String deny;

    public PermissionOverwritePayload(int type, String allowBitmask, String denyBitmask) {

        this.type = type;
        this.allow = allowBitmask;
        this.deny = denyBitmask;
    }

    public int getType() {
        return type;
    }

    public String getAllow() {
        return allow;
    }

    public String getDeny() {
        return deny;
    }

}
