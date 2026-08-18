package flux.rest.api;

import flux.gateway.client.RestClient;
import flux.rest.Urls;
import okhttp3.MultipartBody;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public final class VoiceApi {
    private final RestClient http;

    public VoiceApi(RestClient http) {
        this.http = http;
    }

    public CompletableFuture<String> playEntranceSound(String channelId, String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("channel_id", channelId);
        String url = Urls.resolve("/voice/channels/{channel_id}/entrance-sound", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

}
