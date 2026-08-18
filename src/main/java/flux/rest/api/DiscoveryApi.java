package flux.rest.api;

import flux.gateway.client.RestClient;
import flux.rest.Urls;
import okhttp3.MultipartBody;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public final class DiscoveryApi {
    private final RestClient http;

    public DiscoveryApi(RestClient http) {
        this.http = http;
    }

    public CompletableFuture<String> listDiscoveryCategories() {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/discovery/categories", pathParams, Map.of());
        return http.request("GET", url, null, Map.of());
    }

    public CompletableFuture<String> searchDiscoveryGuilds(Map<String, String> query) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/discovery/guilds", pathParams, query);
        return http.request("GET", url, null, Map.of());
    }

    public CompletableFuture<String> joinDiscoveryGuild(String guildId) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("guild_id", guildId);
        String url = Urls.resolve("/discovery/guilds/{guild_id}/join", pathParams, Map.of());
        return http.request("POST", url, null, Map.of());
    }

    public CompletableFuture<String> applyForDiscovery(String guildId, String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("guild_id", guildId);
        String url = Urls.resolve("/guilds/{guild_id}/discovery", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> editDiscoveryApplication(String guildId, String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("guild_id", guildId);
        String url = Urls.resolve("/guilds/{guild_id}/discovery", pathParams, Map.of());
        return http.request("PATCH", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> withdrawDiscoveryApplication(String guildId) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("guild_id", guildId);
        String url = Urls.resolve("/guilds/{guild_id}/discovery", pathParams, Map.of());
        return http.request("DELETE", url, null, Map.of());
    }

    public CompletableFuture<String> getDiscoveryStatus(String guildId) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("guild_id", guildId);
        String url = Urls.resolve("/guilds/{guild_id}/discovery", pathParams, Map.of());
        return http.request("GET", url, null, Map.of());
    }

}
