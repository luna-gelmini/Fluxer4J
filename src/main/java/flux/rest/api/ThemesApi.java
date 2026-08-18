package flux.rest.api;

import flux.gateway.client.RestClient;
import flux.rest.Urls;
import okhttp3.MultipartBody;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public final class ThemesApi {
    private final RestClient http;

    public ThemesApi(RestClient http) {
        this.http = http;
    }

    public CompletableFuture<String> createTheme(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/users/@me/themes", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

}
