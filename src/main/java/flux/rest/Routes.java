package flux.rest;

import java.util.Map;

public final class Routes {
    private Routes() {
    }

    public static String base() {
        return flux.api.ApiEnvironment.getApiBaseUrl();
    }

    public static String gatewayBot() {
        return Urls.resolve("/gateway/bot", Map.of(), Map.of());
    }

    public static String user(String userId) {
        return Urls.resolve("/users/{user_id}", Map.of("user_id", userId), Map.of());
    }

    public static String channel(String channelId) {
        return Urls.resolve("/channels/{channel_id}", Map.of("channel_id", channelId), Map.of());
    }

    public static String channelMessages(String channelId) {
        return Urls.resolve("/channels/{channel_id}/messages", Map.of("channel_id", channelId), Map.of());
    }

    public static String channelMessages(String channelId, int limit) {
        return Urls.resolve("/channels/{channel_id}/messages", Map.of("channel_id", channelId),
                Map.of("limit", Integer.toString(limit)));
    }

    public static String channelMessage(String channelId, String messageId) {
        return Urls.resolve("/channels/{channel_id}/messages/{message_id}",
                Map.of("channel_id", channelId, "message_id", messageId), Map.of());
    }

    public static String bulkDeleteMessages(String channelId) {
        return Urls.resolve("/channels/{channel_id}/messages/bulk-delete",
                Map.of("channel_id", channelId), Map.of());
    }

    public static String typing(String channelId) {
        return Urls.resolve("/channels/{channel_id}/typing", Map.of("channel_id", channelId), Map.of());
    }

    public static String channelPermission(String channelId, String overwriteId) {
        return Urls.resolve("/channels/{channel_id}/permissions/{overwrite_id}",
                Map.of("channel_id", channelId, "overwrite_id", overwriteId), Map.of());
    }

    public static String guild(String guildId) {
        return Urls.resolve("/guilds/{guild_id}", Map.of("guild_id", guildId), Map.of());
    }

    public static String guildChannels(String guildId) {
        return Urls.resolve("/guilds/{guild_id}/channels", Map.of("guild_id", guildId), Map.of());
    }

    public static String guildRoles(String guildId) {
        return Urls.resolve("/guilds/{guild_id}/roles", Map.of("guild_id", guildId), Map.of());
    }

    public static String guildMember(String guildId, String userId) {
        return Urls.resolve("/guilds/{guild_id}/members/{user_id}",
                Map.of("guild_id", guildId, "user_id", userId), Map.of());
    }

    public static String guildMemberRole(String guildId, String userId, String roleId) {
        return Urls.resolve("/guilds/{guild_id}/members/{user_id}/roles/{role_id}",
                Map.of("guild_id", guildId, "user_id", userId, "role_id", roleId), Map.of());
    }

    public static String guildBan(String guildId, String userId) {
        return Urls.resolve("/guilds/{guild_id}/bans/{user_id}",
                Map.of("guild_id", guildId, "user_id", userId), Map.of());
    }

    public static String entranceSound(String channelId) {
        return Urls.resolve("/voice/channels/{channel_id}/entrance-sound",
                Map.of("channel_id", channelId), Map.of());
    }

    public static String webhookExecute(String webhookId, String token) {
        return Urls.resolve("/webhooks/{webhook_id}/{token}",
                Map.of("webhook_id", webhookId, "token", token), Map.of());
    }
}
