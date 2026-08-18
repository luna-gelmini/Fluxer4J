package flux.rest.api;

import flux.gateway.client.RestClient;
import flux.rest.Urls;
import okhttp3.MultipartBody;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public final class InvitesApi {
    private final RestClient http;

    public InvitesApi(RestClient http) {
        this.http = http;
    }

    public CompletableFuture<String> createChannelInvite(String channelId, String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("channel_id", channelId);
        String url = Urls.resolve("/channels/{channel_id}/invites", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> listChannelInvites(String channelId) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("channel_id", channelId);
        String url = Urls.resolve("/channels/{channel_id}/invites", pathParams, Map.of());
        return http.request("GET", url, null, Map.of());
    }

    public CompletableFuture<String> listGuildInvites(String guildId) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("guild_id", guildId);
        String url = Urls.resolve("/guilds/{guild_id}/invites", pathParams, Map.of());
        return http.request("GET", url, null, Map.of());
    }

    public CompletableFuture<String> getInvite(String inviteCode) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("invite_code", inviteCode);
        String url = Urls.resolve("/invites/{invite_code}", pathParams, Map.of());
        return http.request("GET", url, null, Map.of());
    }

    public CompletableFuture<String> acceptInvite(String inviteCode) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("invite_code", inviteCode);
        String url = Urls.resolve("/invites/{invite_code}", pathParams, Map.of());
        return http.request("POST", url, null, Map.of());
    }

    public CompletableFuture<String> deleteInvite(String inviteCode) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("invite_code", inviteCode);
        String url = Urls.resolve("/invites/{invite_code}", pathParams, Map.of());
        return http.request("DELETE", url, null, Map.of());
    }

    public CompletableFuture<String> listPackInvites(String packId) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("pack_id", packId);
        String url = Urls.resolve("/packs/{pack_id}/invites", pathParams, Map.of());
        return http.request("GET", url, null, Map.of());
    }

    public CompletableFuture<String> createPackInvite(String packId, String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("pack_id", packId);
        String url = Urls.resolve("/packs/{pack_id}/invites", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

}
