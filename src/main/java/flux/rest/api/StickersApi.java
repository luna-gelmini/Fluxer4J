package flux.rest.api;

import flux.gateway.client.RestClient;
import flux.rest.Urls;
import okhttp3.MultipartBody;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public final class StickersApi {
    private final RestClient http;

    public StickersApi(RestClient http) {
        this.http = http;
    }

    public CompletableFuture<String> getStickerMetadata(String stickerId) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("sticker_id", stickerId);
        String url = Urls.resolve("/stickers/{sticker_id}/metadata", pathParams, Map.of());
        return http.request("GET", url, null, Map.of());
    }

}
