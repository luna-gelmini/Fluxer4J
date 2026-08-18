package flux.rest.api;

import flux.gateway.client.RestClient;
import flux.rest.Urls;
import okhttp3.MultipartBody;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public final class GiftsApi {
    private final RestClient http;

    public GiftsApi(RestClient http) {
        this.http = http;
    }

    public CompletableFuture<String> getGiftCode(String code) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("code", code);
        String url = Urls.resolve("/gifts/{code}", pathParams, Map.of());
        return http.request("GET", url, null, Map.of());
    }

    public CompletableFuture<String> redeemGiftCode(String code) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("code", code);
        String url = Urls.resolve("/gifts/{code}/redeem", pathParams, Map.of());
        return http.request("POST", url, null, Map.of());
    }

}
