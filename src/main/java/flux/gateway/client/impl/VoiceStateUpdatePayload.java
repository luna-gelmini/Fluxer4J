package flux.gateway.client.impl;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.Nullable;

public class VoiceStateUpdatePayload {

    @JsonProperty("guild_id")
    private final String guildId;

    @JsonProperty("channel_id")
    @JsonInclude(JsonInclude.Include.ALWAYS)
    private final String channelId;

    @JsonProperty("self_mute")
    private final boolean selfMute;

    @JsonProperty("self_deaf")
    private final boolean selfDeaf;

    @JsonProperty("soundboard_sound_id")
    private String soundboardSoundId;

    public VoiceStateUpdatePayload(String guildId, @Nullable String channelId, boolean selfMute, boolean selfDeaf) {
        this.guildId = guildId;
        this.channelId = channelId;
        this.selfMute = selfMute;
        this.selfDeaf = selfDeaf;
    }

    public void setSoundboardSoundId(String soundboardSoundId) {
        this.soundboardSoundId = soundboardSoundId;
    }
}
