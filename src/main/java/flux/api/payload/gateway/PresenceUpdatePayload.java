package flux.api.payload.gateway;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class PresenceUpdatePayload {
    @JsonProperty("since")
    private final Long since = null;
    @JsonProperty("activities")
    private final List<ActivityPayload> activities;
    @JsonProperty("status")
    private final String status = "online";
    @JsonProperty("afk")
    private final boolean afk = false;

    public PresenceUpdatePayload(List<ActivityPayload> activities) {
        this.activities = activities;
    }

    public Long getSince() {
        return null;
    }

    public List<ActivityPayload> getActivities() {
        return activities;
    }

    public String getStatus() {
        return status;
    }

    public boolean isAfk() {
        return afk;
    }
}
