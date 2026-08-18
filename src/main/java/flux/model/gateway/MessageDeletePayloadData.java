package flux.model.gateway;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MessageDeletePayloadData {
    @JsonProperty("id")
    private String id;

    @JsonProperty("channel_id")
    private String channelId;

    @JsonProperty("guild_id")
    private String guildId;

    public String getId() {
        return id;
    }

    public String getChannelId() {
        return channelId;
    }

    public String getGuildId() {
        return guildId;
    }
}
