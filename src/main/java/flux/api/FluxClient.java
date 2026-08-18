package flux.api;

import flux.api.entities.*;
import flux.rest.FluxRest;
import flux.api.entities.channel.Channel;
import flux.api.entities.channel.ChannelType;
import flux.api.event.EventListener;
import flux.api.gateway.GatewayIntent;
import flux.api.payload.channel.ChannelModifyPayload;
import flux.api.payload.channel.CreateGuildChannelPayload;
import flux.api.payload.interaction.CreateCommandPayload;
import flux.api.payload.permission.Permission;
import flux.api.payload.send.MessageSendPayload;
import flux.api.voice.VoiceConnection;
import okhttp3.MultipartBody;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface FluxClient {

    User getSelfUser();

    FluxRest rest();

    CompletableFuture<Void> login(String token, Collection<GatewayIntent> intents);

    void shutdown();

    CompletableFuture<Message> sendMessage(String channelId, String content);

    void addEventListener(EventListener listener);

    void removeEventListener(EventListener listener);

    CompletableFuture<User> getUserById(String userId);

    CompletableFuture<Channel> getChannelById(String channelId);

    CompletableFuture<Guild> getGuildById(String guildId);

    CompletableFuture<List<Role>> getGuildRoles(String guildId);

    CompletableFuture<Member> getGuildMember(String guildId, String userId);

    default CompletableFuture<Member> retrieveMemberById(String userId, String guildId) {
        return getGuildMember(guildId, userId);
    }

    CompletableFuture<Void> addRoleToMember(String guildId, String userId, String roleId);

    CompletableFuture<Void> removeRoleFromMember(String guildId, String userId, String roleId);

    CompletableFuture<Message> sendMessage(String channelId, MessageSendPayload messageData);

    CompletableFuture<Message> editMessage(String channelId, String messageId, MessageSendPayload messageData);

    CompletableFuture<Channel> createGuildChannel(String guildId, CreateGuildChannelPayload payload);

    CompletableFuture<Channel> createGuildChannel(String guildId, String name, ChannelType type,
            @Nullable String parentCategoryId);

    CompletableFuture<Channel> deleteChannel(String channelId);

    CompletableFuture<Void> modifyGuildMemberVoiceChannel(String guildId, String userId,
            @Nullable String voiceChannelId);

    CompletableFuture<Channel> modifyChannel(String channelId, ChannelModifyPayload payload);

    CompletableFuture<Void> editChannelPermissions(String channelId, String targetId, TargetType type,
            Collection<Permission> allow, Collection<Permission> deny);

    CompletableFuture<Void> editChannelPermissions(String channelId, String targetId, TargetType type,
            long allowBitmask, long denyBitmask);

    CompletableFuture<Message> sendMessage(String channelId, MultipartBody body);

    void playSoundboardSound(String guildId, String channelId, String soundId);

    CompletableFuture<VoiceConnection> joinVoiceChannel(String guildId, String channelId);

    CompletableFuture<Void> leaveVoiceChannel(String guildId);

    void setActivity(ActivityType type, String name, @Nullable String url);

    default void setActivity(ActivityType type, String name) {
        if (type == ActivityType.STREAMING) {
            throw new IllegalArgumentException(
                    "STREAMING activity type requires a URL. Use the setActivity(type, name, url) method.");
        }
        setActivity(type, name, null);
    }

    CompletableFuture<Void> createGlobalCommand(CreateCommandPayload payload);

    CompletableFuture<Void> createGuildCommand(String guildId, CreateCommandPayload payload);

    CompletableFuture<Void> bulkOverwriteGlobalCommands(List<CreateCommandPayload> payloads);
    CompletableFuture<String> sendInteractionFollowup(String applicationId, String token, okhttp3.MultipartBody body);

    CompletableFuture<Void> banMember(String guildId, String userId, String reason, int deleteMessageSeconds);

    CompletableFuture<Void> kickMember(String guildId, String userId, String reason);

    CompletableFuture<Void> timeoutMember(String guildId, String userId, Integer durationSeconds, String reason);

    CompletableFuture<Void> bulkDeleteMessages(String channelId, List<String> messageIds);

    CompletableFuture<Void> deleteMessage(String channelId, String messageId);

    CompletableFuture<List<Message>> getMessages(String channelId, int limit);

    CompletableFuture<Void> createInteractionResponse(String interactionId, String interactionToken,
            String jsonPayload);

    CompletableFuture<Void> sendTyping(String channelId);

    interface AudioReceiveHandler {
        boolean canReceiveUser(User user);

        void handleUserAudio(User user, byte[] pcmData);

        void onShutdown();
    }

    enum ActivityType {
        PLAYING(0),
        STREAMING(1),
        LISTENING(2),
        WATCHING(3),
        CUSTOM(4),
        COMPETING(5);

        private final int value;

        ActivityType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
