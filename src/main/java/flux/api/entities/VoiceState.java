package flux.api.entities;

public interface VoiceState {
    String getGuildId();

    String getChannelId();

    String getUserId();

    String getSessionId();

    boolean isDeaf();

    boolean isMute();

    boolean isSelfDeaf();

    boolean isSelfMute();

    Boolean isSelfStream();

    boolean isSelfVideo();

    boolean isSuppress();

    String getRequestToSpeakTimestamp();

}
