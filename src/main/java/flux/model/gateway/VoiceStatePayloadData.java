package flux.model.gateway;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VoiceStatePayloadData {
    @JsonProperty("guild_id")
    private String guildId;

    @JsonProperty("channel_id")
    private String channelId;

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("session_id")
    private String sessionId;

    @JsonProperty("connection_id")
    private String connectionId;

    @JsonProperty("deaf")
    private boolean deaf;

    @JsonProperty("mute")
    private boolean mute;

    @JsonProperty("self_deaf")
    private boolean selfDeaf;

    @JsonProperty("self_mute")
    private boolean selfMute;

    @JsonProperty("self_stream")
    private Boolean selfStream;

    @JsonProperty("self_video")
    private boolean selfVideo;

    @JsonProperty("suppress")
    private boolean suppress;

    public String getGuildId() {
        return guildId;
    }

    public String getChannelId() {
        return channelId;
    }

    public String getUserId() {
        return userId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getConnectionId() {
        return connectionId != null && !connectionId.isBlank() ? connectionId : sessionId;
    }

    public boolean isDeaf() {
        return deaf;
    }

    public boolean isMute() {
        return mute;
    }

    public boolean isSelfDeaf() {
        return selfDeaf;
    }

    public boolean isSelfMute() {
        return selfMute;
    }

    public Boolean getSelfStream() {
        return selfStream;
    }

    public boolean isSelfVideo() {
        return selfVideo;
    }

    public boolean isSuppress() {
        return suppress;
    }
}
