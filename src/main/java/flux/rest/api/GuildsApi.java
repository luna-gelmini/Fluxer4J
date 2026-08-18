package flux.rest.api;

import flux.gateway.client.RestClient;
import flux.rest.Urls;
import okhttp3.MultipartBody;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public final class GuildsApi {
    private final RestClient http;

    public GuildsApi(RestClient http) {
        this.http = http;
    }

    public CompletableFuture<String> createGuild(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/guilds", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> getGuild(String guildId) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("guild_id", guildId);
        String url = Urls.resolve("/guilds/{guild_id}", pathParams, Map.of());
        return http.request("GET", url, null, Map.of());
    }

    public CompletableFuture<String> updateGuild(String guildId, String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("guild_id", guildId);
        String url = Urls.resolve("/guilds/{guild_id}", pathParams, Map.of());
        return http.request("PATCH", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> listGuildAuditLogs(String guildId, Map<String, String> query) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("guild_id", guildId);
        String url = Urls.resolve("/guilds/{guild_id}/audit-logs", pathParams, query);
        return http.request("GET", url, null, Map.of());
    }

    public CompletableFuture<String> listGuildBans(String guildId) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("guild_id", guildId);
        String url = Urls.resolve("/guilds/{guild_id}/bans", pathParams, Map.of());
        return http.request("GET", url, null, Map.of());
    }

    public CompletableFuture<String> banGuildMember(String guildId, String userId, String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("guild_id", guildId);
        pathParams.put("user_id", userId);
        String url = Urls.resolve("/guilds/{guild_id}/bans/{user_id}", pathParams, Map.of());
        return http.request("PUT", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> unbanGuildMember(String guildId, String userId) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("guild_id", guildId);
        pathParams.put("user_id", userId);
        String url = Urls.resolve("/guilds/{guild_id}/bans/{user_id}", pathParams, Map.of());
        return http.request("DELETE", url, null, Map.of());
    }

    public CompletableFuture<String> listGuildChannels(String guildId) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("guild_id", guildId);
        String url = Urls.resolve("/guilds/{guild_id}/channels", pathParams, Map.of());
        return http.request("GET", url, null, Map.of());
    }

    public CompletableFuture<String> createGuildChannel(String guildId, String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("guild_id", guildId);
        String url = Urls.resolve("/guilds/{guild_id}/channels", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> updateGuildChannelPositions(String guildId, String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("guild_id", guildId);
        String url = Urls.resolve("/guilds/{guild_id}/channels", pathParams, Map.of());
        return http.request("PATCH", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> deleteGuild(String guildId, String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("guild_id", guildId);
        String url = Urls.resolve("/guilds/{guild_id}/delete", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> createGuildEmoji(String guildId, String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("guild_id", guildId);
        String url = Urls.resolve("/guilds/{guild_id}/emojis", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> listGuildEmojis(String guildId) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("guild_id", guildId);
        String url = Urls.resolve("/guilds/{guild_id}/emojis", pathParams, Map.of());
        return http.request("GET", url, null, Map.of());
    }

    public CompletableFuture<String> bulkCreateGuildEmojis(String guildId, String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("guild_id", guildId);
        String url = Urls.resolve("/guilds/{guild_id}/emojis/bulk", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> cloneGuildEmoji(String guildId, String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("guild_id", guildId);
        String url = Urls.resolve("/guilds/{guild_id}/emojis/clone", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> updateGuildEmoji(String guildId, String emojiId, String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("guild_id", guildId);
        pathParams.put("emoji_id", emojiId);
        String url = Urls.resolve("/guilds/{guild_id}/emojis/{emoji_id}", pathParams, Map.of());
        return http.request("PATCH", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> deleteGuildEmoji(String guildId, String emojiId, Map<String, String> query) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("guild_id", guildId);
        pathParams.put("emoji_id", emojiId);
        String url = Urls.resolve("/guilds/{guild_id}/emojis/{emoji_id}", pathParams, query);
        return http.request("DELETE", url, null, Map.of());
    }

    public CompletableFuture<String> listGuildMembers(String guildId, Map<String, String> query) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("guild_id", guildId);
        String url = Urls.resolve("/guilds/{guild_id}/members", pathParams, query);
        return http.request("GET", url, null, Map.of());
    }

    public CompletableFuture<String> searchGuildMembers(String guildId, String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("guild_id", guildId);
        String url = Urls.resolve("/guilds/{guild_id}/members-search", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> getCurrentGuildMember(String guildId) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("guild_id", guildId);
        String url = Urls.resolve("/guilds/{guild_id}/members/@me", pathParams, Map.of());
        return http.request("GET", url, null, Map.of());
    }

    public CompletableFuture<String> updateCurrentGuildMember(String guildId, String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("guild_id", guildId);
        String url = Urls.resolve("/guilds/{guild_id}/members/@me", pathParams, Map.of());
        return http.request("PATCH", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> getGuildMember(String guildId, String userId) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("guild_id", guildId);
        pathParams.put("user_id", userId);
        String url = Urls.resolve("/guilds/{guild_id}/members/{user_id}", pathParams, Map.of());
        return http.request("GET", url, null, Map.of());
    }

    public CompletableFuture<String> updateGuildMember(String guildId, String userId, String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("guild_id", guildId);
        pathParams.put("user_id", userId);
        String url = Urls.resolve("/guilds/{guild_id}/members/{user_id}", pathParams, Map.of());
        return http.request("PATCH", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> removeGuildMember(String guildId, String userId) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("guild_id", guildId);
        pathParams.put("user_id", userId);
        String url = Urls.resolve("/guilds/{guild_id}/members/{user_id}", pathParams, Map.of());
        return http.request("DELETE", url, null, Map.of());
    }

    public CompletableFuture<String> addGuildMemberRole(String guildId, String userId, String roleId) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("guild_id", guildId);
        pathParams.put("user_id", userId);
        pathParams.put("role_id", roleId);
        String url = Urls.resolve("/guilds/{guild_id}/members/{user_id}/roles/{role_id}", pathParams, Map.of());
        return http.request("PUT", url, null, Map.of());
    }

    public CompletableFuture<String> removeGuildMemberRole(String guildId, String userId, String roleId) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("guild_id", guildId);
        pathParams.put("user_id", userId);
        pathParams.put("role_id", roleId);
        String url = Urls.resolve("/guilds/{guild_id}/members/{user_id}/roles/{role_id}", pathParams, Map.of());
        return http.request("DELETE", url, null, Map.of());
    }

    public CompletableFuture<String> listGuildRoles(String guildId) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("guild_id", guildId);
        String url = Urls.resolve("/guilds/{guild_id}/roles", pathParams, Map.of());
        return http.request("GET", url, null, Map.of());
    }

    public CompletableFuture<String> createGuildRole(String guildId, String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("guild_id", guildId);
        String url = Urls.resolve("/guilds/{guild_id}/roles", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> updateGuildRolePositions(String guildId, String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("guild_id", guildId);
        String url = Urls.resolve("/guilds/{guild_id}/roles", pathParams, Map.of());
        return http.request("PATCH", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> updateRoleHoistPositions(String guildId, String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("guild_id", guildId);
        String url = Urls.resolve("/guilds/{guild_id}/roles/hoist-positions", pathParams, Map.of());
        return http.request("PATCH", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> resetRoleHoistPositions(String guildId) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("guild_id", guildId);
        String url = Urls.resolve("/guilds/{guild_id}/roles/hoist-positions", pathParams, Map.of());
        return http.request("DELETE", url, null, Map.of());
    }

    public CompletableFuture<String> updateGuildRole(String guildId, String roleId, String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("guild_id", guildId);
        pathParams.put("role_id", roleId);
        String url = Urls.resolve("/guilds/{guild_id}/roles/{role_id}", pathParams, Map.of());
        return http.request("PATCH", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> deleteGuildRole(String guildId, String roleId) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("guild_id", guildId);
        pathParams.put("role_id", roleId);
        String url = Urls.resolve("/guilds/{guild_id}/roles/{role_id}", pathParams, Map.of());
        return http.request("DELETE", url, null, Map.of());
    }

    public CompletableFuture<String> createGuildSticker(String guildId, String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("guild_id", guildId);
        String url = Urls.resolve("/guilds/{guild_id}/stickers", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> listGuildStickers(String guildId) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("guild_id", guildId);
        String url = Urls.resolve("/guilds/{guild_id}/stickers", pathParams, Map.of());
        return http.request("GET", url, null, Map.of());
    }

    public CompletableFuture<String> bulkCreateGuildStickers(String guildId, String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("guild_id", guildId);
        String url = Urls.resolve("/guilds/{guild_id}/stickers/bulk", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> cloneGuildSticker(String guildId, String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("guild_id", guildId);
        String url = Urls.resolve("/guilds/{guild_id}/stickers/clone", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> updateGuildSticker(String guildId, String stickerId, String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("guild_id", guildId);
        pathParams.put("sticker_id", stickerId);
        String url = Urls.resolve("/guilds/{guild_id}/stickers/{sticker_id}", pathParams, Map.of());
        return http.request("PATCH", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> deleteGuildSticker(String guildId, String stickerId, Map<String, String> query) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("guild_id", guildId);
        pathParams.put("sticker_id", stickerId);
        String url = Urls.resolve("/guilds/{guild_id}/stickers/{sticker_id}", pathParams, query);
        return http.request("DELETE", url, null, Map.of());
    }

    public CompletableFuture<String> transferGuildOwnership(String guildId, String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("guild_id", guildId);
        String url = Urls.resolve("/guilds/{guild_id}/transfer-ownership", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> getGuildVanityUrl(String guildId) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("guild_id", guildId);
        String url = Urls.resolve("/guilds/{guild_id}/vanity-url", pathParams, Map.of());
        return http.request("GET", url, null, Map.of());
    }

    public CompletableFuture<String> updateGuildVanityUrl(String guildId, String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("guild_id", guildId);
        String url = Urls.resolve("/guilds/{guild_id}/vanity-url", pathParams, Map.of());
        return http.request("PATCH", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> listGuilds(Map<String, String> query) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/users/@me/guilds", pathParams, query);
        return http.request("GET", url, null, Map.of());
    }

    public CompletableFuture<String> leaveGuild(String guildId, Map<String, String> query, String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("guild_id", guildId);
        String url = Urls.resolve("/users/@me/guilds/{guild_id}", pathParams, query);
        return http.request("DELETE", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> bulkDeleteMyMessagesInGuild(String guildId, String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("guild_id", guildId);
        String url = Urls.resolve("/users/@me/guilds/{guild_id}/messages/bulk-delete-mine", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

}
