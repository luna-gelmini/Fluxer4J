package flux.rest.api;

import flux.gateway.client.RestClient;
import flux.rest.Urls;
import okhttp3.MultipartBody;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public final class SavedMediaApi {
    private final RestClient http;

    public SavedMediaApi(RestClient http) {
        this.http = http;
    }

    public CompletableFuture<String> createMemeFromMessage(String channelId, String messageId, String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("channel_id", channelId);
        pathParams.put("message_id", messageId);
        String url = Urls.resolve("/channels/{channel_id}/messages/{message_id}/memes", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> resolveGifUrls(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/users/@me/favorite-gifs/resolve", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> listFavoriteMemes() {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/users/@me/memes", pathParams, Map.of());
        return http.request("GET", url, null, Map.of());
    }

    public CompletableFuture<String> createMemeFromUrl(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/users/@me/memes", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> getFavoriteMeme(String memeId) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("meme_id", memeId);
        String url = Urls.resolve("/users/@me/memes/{meme_id}", pathParams, Map.of());
        return http.request("GET", url, null, Map.of());
    }

    public CompletableFuture<String> updateFavoriteMeme(String memeId, String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("meme_id", memeId);
        String url = Urls.resolve("/users/@me/memes/{meme_id}", pathParams, Map.of());
        return http.request("PATCH", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> deleteFavoriteMeme(String memeId) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("meme_id", memeId);
        String url = Urls.resolve("/users/@me/memes/{meme_id}", pathParams, Map.of());
        return http.request("DELETE", url, null, Map.of());
    }

}
