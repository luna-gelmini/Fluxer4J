package flux.api.entities.channel;

public interface VoiceChannel extends Channel {
    Integer getUserLimit();

    Integer getBitrate();

    String getRtcRegion();

    String getStatus();

    String getParentId();

}
