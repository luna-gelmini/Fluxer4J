package flux.api.event.voice;

import flux.api.FluxClient;
import flux.api.entities.Guild;
import flux.api.entities.Member;
import flux.api.entities.User;
import flux.api.entities.channel.Channel;
import flux.api.event.AbstractEvent;

import java.util.concurrent.CompletableFuture;

public class VoiceStateUpdateEvent extends AbstractEvent {

    private final String guildId;
    private final String channelId;
    private final String userId;
    private final boolean isMuted;
    private final boolean isDeafened;

    public VoiceStateUpdateEvent(FluxClient fluxClient,
                                 String guildId, String channelId, String userId,
                                 boolean isMuted, boolean isDeafened) {
        super(fluxClient);
        this.guildId = guildId;
        this.channelId = channelId;
        this.userId = userId;
        this.isMuted = isMuted;
        this.isDeafened = isDeafened;

    }

    public String getGuildId() {
        return guildId;
    }

    public String getChannelId() {
        return channelId;
    }

    public String getUserId() {
        return userId;
    }

    public boolean isMuted() {
        return isMuted;
    }

    public boolean isDeafened() {
        return isDeafened;
    }

    public CompletableFuture<User> retrieveUser() {
        return fluxClient.getUserById(userId);
    }

    public CompletableFuture<Member> retrieveMember() {
        if (guildId == null) return CompletableFuture.failedFuture(new IllegalStateException("Not in a guild."));
        return fluxClient.getGuildMember(guildId, userId);
    }

    public CompletableFuture<Guild> retrieveGuild() {
        if (guildId == null) return CompletableFuture.failedFuture(new IllegalStateException("Not in a guild."));
        return fluxClient.getGuildById(guildId);
    }

    public CompletableFuture<Channel> retrieveChannel() {
        if (channelId == null) return CompletableFuture.completedFuture(null);
        return fluxClient.getChannelById(channelId);
    }

    public boolean hasJoinedChannel() {
        return channelId != null;
    }

    public boolean hasLeftChannel() {

        return channelId == null;
    }
}
