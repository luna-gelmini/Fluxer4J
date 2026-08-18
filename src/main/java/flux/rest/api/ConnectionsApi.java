package flux.rest.api;

import flux.gateway.client.RestClient;
import flux.rest.Urls;
import okhttp3.MultipartBody;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public final class ConnectionsApi {
    private final RestClient http;

    public ConnectionsApi(RestClient http) {
        this.http = http;
    }

    public CompletableFuture<String> listConnections() {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/users/@me/connections", pathParams, Map.of());
        return http.request("GET", url, null, Map.of());
    }

    public CompletableFuture<String> initiateConnection(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/users/@me/connections", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> authorizeBlueskyConnection(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/users/@me/connections/bluesky/authorize", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> reorderConnections(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/users/@me/connections/reorder", pathParams, Map.of());
        return http.request("PATCH", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> verifyAndCreateConnection(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/users/@me/connections/verify", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> updateConnection(String type, String connectionId, String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("type", type);
        pathParams.put("connection_id", connectionId);
        String url = Urls.resolve("/users/@me/connections/{type}/{connection_id}", pathParams, Map.of());
        return http.request("PATCH", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> deleteConnection(String type, String connectionId) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("type", type);
        pathParams.put("connection_id", connectionId);
        String url = Urls.resolve("/users/@me/connections/{type}/{connection_id}", pathParams, Map.of());
        return http.request("DELETE", url, null, Map.of());
    }

    public CompletableFuture<String> verifyConnection(String type, String connectionId) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("type", type);
        pathParams.put("connection_id", connectionId);
        String url = Urls.resolve("/users/@me/connections/{type}/{connection_id}/verify", pathParams, Map.of());
        return http.request("POST", url, null, Map.of());
    }

}
