package flux.api.gateway;

import flux.api.event.Event;
import org.jetbrains.annotations.Nullable;

public interface EventDispatcher {
    void dispatch(Event event);

    void _internal_leaveVoiceChannel(String guildId);

    void sendVoiceStateUpdate(String guildId, @Nullable String channelId, boolean selfMute, boolean selfDeaf);
}
