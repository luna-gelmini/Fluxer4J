package flux.rest.api;

import flux.gateway.client.RestClient;
import flux.rest.Urls;
import okhttp3.MultipartBody;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public final class GeolocationApi {
    private final RestClient http;

    public GeolocationApi(RestClient http) {
        this.http = http;
    }

    public CompletableFuture<String> getClientGeolocation() {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/ip", pathParams, Map.of());
        return http.request("GET", url, null, Map.of());
    }

}
