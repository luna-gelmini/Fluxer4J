package flux.rest.api;

import flux.gateway.client.RestClient;
import flux.rest.Urls;
import okhttp3.MultipartBody;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public final class PacksApi {
    private final RestClient http;

    public PacksApi(RestClient http) {
        this.http = http;
    }

    public CompletableFuture<String> listUserPacks() {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/packs", pathParams, Map.of());
        return http.request("GET", url, null, Map.of());
    }

    public CompletableFuture<String> createPackEmoji(String packId, String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("pack_id", packId);
        String url = Urls.resolve("/packs/emojis/{pack_id}", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> listPackEmojis(String packId) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("pack_id", packId);
        String url = Urls.resolve("/packs/emojis/{pack_id}", pathParams, Map.of());
        return http.request("GET", url, null, Map.of());
    }

    public CompletableFuture<String> bulkCreatePackEmojis(String packId, String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("pack_id", packId);
        String url = Urls.resolve("/packs/emojis/{pack_id}/bulk", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> updatePackEmoji(String packId, String emojiId, String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("pack_id", packId);
        pathParams.put("emoji_id", emojiId);
        String url = Urls.resolve("/packs/emojis/{pack_id}/{emoji_id}", pathParams, Map.of());
        return http.request("PATCH", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> deletePackEmoji(String packId, String emojiId, Map<String, String> query) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("pack_id", packId);
        pathParams.put("emoji_id", emojiId);
        String url = Urls.resolve("/packs/emojis/{pack_id}/{emoji_id}", pathParams, query);
        return http.request("DELETE", url, null, Map.of());
    }

    public CompletableFuture<String> createPackSticker(String packId, String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("pack_id", packId);
        String url = Urls.resolve("/packs/stickers/{pack_id}", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> listPackStickers(String packId) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("pack_id", packId);
        String url = Urls.resolve("/packs/stickers/{pack_id}", pathParams, Map.of());
        return http.request("GET", url, null, Map.of());
    }

    public CompletableFuture<String> bulkCreatePackStickers(String packId, String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("pack_id", packId);
        String url = Urls.resolve("/packs/stickers/{pack_id}/bulk", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> updatePackSticker(String packId, String stickerId, String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("pack_id", packId);
        pathParams.put("sticker_id", stickerId);
        String url = Urls.resolve("/packs/stickers/{pack_id}/{sticker_id}", pathParams, Map.of());
        return http.request("PATCH", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> deletePackSticker(String packId, String stickerId, Map<String, String> query) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("pack_id", packId);
        pathParams.put("sticker_id", stickerId);
        String url = Urls.resolve("/packs/stickers/{pack_id}/{sticker_id}", pathParams, query);
        return http.request("DELETE", url, null, Map.of());
    }

    public CompletableFuture<String> updatePack(String packId, String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("pack_id", packId);
        String url = Urls.resolve("/packs/{pack_id}", pathParams, Map.of());
        return http.request("PATCH", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> deletePack(String packId) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("pack_id", packId);
        String url = Urls.resolve("/packs/{pack_id}", pathParams, Map.of());
        return http.request("DELETE", url, null, Map.of());
    }

    public CompletableFuture<String> installPack(String packId) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("pack_id", packId);
        String url = Urls.resolve("/packs/{pack_id}/install", pathParams, Map.of());
        return http.request("POST", url, null, Map.of());
    }

    public CompletableFuture<String> uninstallPack(String packId) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("pack_id", packId);
        String url = Urls.resolve("/packs/{pack_id}/install", pathParams, Map.of());
        return http.request("DELETE", url, null, Map.of());
    }

    public CompletableFuture<String> createPack(String packType, String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("pack_type", packType);
        String url = Urls.resolve("/packs/{pack_type}", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

}
