package flux.api.payload.gateway;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ActivityPayload {
    @JsonProperty("name")
    private final String name;
    @JsonProperty("type")
    private final int type;

    public ActivityPayload(String name, int type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }
}
