package flux.rest.api;

import flux.gateway.client.RestClient;
import flux.rest.Urls;
import okhttp3.MultipartBody;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public final class SearchApi {
    private final RestClient http;

    public SearchApi(RestClient http) {
        this.http = http;
    }

    public CompletableFuture<String> searchMessages(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/search/messages", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

}
