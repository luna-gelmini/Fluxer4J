package flux.rest.api;

import flux.gateway.client.RestClient;
import flux.rest.Urls;
import okhttp3.MultipartBody;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public final class DebugApi {
    private final RestClient http;

    public DebugApi(RestClient http) {
        this.http = http;
    }

    public CompletableFuture<String> debugUnfurl(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/unfurl", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

}
