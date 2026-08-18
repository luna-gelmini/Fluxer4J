package flux.rest.api;

import flux.gateway.client.RestClient;
import flux.rest.Urls;
import okhttp3.MultipartBody;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public final class EmojisApi {
    private final RestClient http;

    public EmojisApi(RestClient http) {
        this.http = http;
    }

    public CompletableFuture<String> getEmojiMetadata(String emojiId) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("emoji_id", emojiId);
        String url = Urls.resolve("/emojis/{emoji_id}/metadata", pathParams, Map.of());
        return http.request("GET", url, null, Map.of());
    }

}
