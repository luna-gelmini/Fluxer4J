package flux.rest.api;

import flux.gateway.client.RestClient;
import flux.rest.Urls;
import okhttp3.MultipartBody;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public final class GatewayApi {
    private final RestClient http;

    public GatewayApi(RestClient http) {
        this.http = http;
    }

    public CompletableFuture<String> getGatewayBot() {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/gateway/bot", pathParams, Map.of());
        return http.request("GET", url, null, Map.of());
    }

}
