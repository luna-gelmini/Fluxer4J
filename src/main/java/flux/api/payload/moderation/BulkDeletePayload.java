package flux.api.payload.moderation;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class BulkDeletePayload {
    @JsonProperty("messages")
    private final List<String> messages;

    public BulkDeletePayload(List<String> messages) {
        this.messages = messages;
    }
}
