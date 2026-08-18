package flux.model.voice;

import com.fasterxml.jackson.annotation.JsonProperty;
import flux.api.entities.VoiceState;

public class VoiceStateImpl implements VoiceState {
    @JsonProperty("guild_id")
    private String guildId;

    @JsonProperty("channel_id")
    private String channelId;
    @JsonProperty("user_id")
    private String userId;
    @JsonProperty("session_id")
    private String sessionId;
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
    @JsonProperty("request_to_speak_timestamp")
    private String requestToSpeakTimestamp;

    public VoiceStateImpl() {
    }

    @Override
    public String getGuildId() {
        return guildId;
    }

    @Override
    public String getChannelId() {
        return channelId;
    }

    @Override
    public String getUserId() {
        return userId;
    }

    @Override
    public String getSessionId() {
        return sessionId;
    }

    @Override
    public boolean isDeaf() {
        return deaf;
    }

    @Override
    public boolean isMute() {
        return mute;
    }

    @Override
    public boolean isSelfDeaf() {
        return selfDeaf;
    }

    @Override
    public boolean isSelfMute() {
        return selfMute;
    }

    @Override
    public Boolean isSelfStream() {
        return selfStream;
    }

    @Override
    public boolean isSelfVideo() {
        return selfVideo;
    }

    @Override
    public String getRequestToSpeakTimestamp() {
        return requestToSpeakTimestamp;
    }

    @Override
    public boolean isSuppress() {
        return suppress;
    }

    public void setGuildId(String guildId) {
        this.guildId = guildId;
    }
}
