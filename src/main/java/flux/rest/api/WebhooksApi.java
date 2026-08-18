package flux.rest.api;

import flux.gateway.client.RestClient;
import flux.rest.Urls;
import okhttp3.MultipartBody;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public final class WebhooksApi {
    private final RestClient http;

    public WebhooksApi(RestClient http) {
        this.http = http;
    }

    public CompletableFuture<String> listChannelWebhooks(String channelId) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("channel_id", channelId);
        String url = Urls.resolve("/channels/{channel_id}/webhooks", pathParams, Map.of());
        return http.request("GET", url, null, Map.of());
    }

    public CompletableFuture<String> createWebhook(String channelId, String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("channel_id", channelId);
        String url = Urls.resolve("/channels/{channel_id}/webhooks", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> listGuildWebhooks(String guildId) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("guild_id", guildId);
        String url = Urls.resolve("/guilds/{guild_id}/webhooks", pathParams, Map.of());
        return http.request("GET", url, null, Map.of());
    }

    public CompletableFuture<String> getWebhook(String webhookId) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("webhook_id", webhookId);
        String url = Urls.resolve("/webhooks/{webhook_id}", pathParams, Map.of());
        return http.request("GET", url, null, Map.of());
    }

    public CompletableFuture<String> updateWebhook(String webhookId, String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("webhook_id", webhookId);
        String url = Urls.resolve("/webhooks/{webhook_id}", pathParams, Map.of());
        return http.request("PATCH", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> deleteWebhook(String webhookId) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("webhook_id", webhookId);
        String url = Urls.resolve("/webhooks/{webhook_id}", pathParams, Map.of());
        return http.request("DELETE", url, null, Map.of());
    }

    public CompletableFuture<String> getWebhookWithToken(String webhookId, String token) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("webhook_id", webhookId);
        pathParams.put("token", token);
        String url = Urls.resolve("/webhooks/{webhook_id}/{token}", pathParams, Map.of());
        return http.request("GET", url, null, Map.of());
    }

    public CompletableFuture<String> updateWebhookWithToken(String webhookId, String token, String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("webhook_id", webhookId);
        pathParams.put("token", token);
        String url = Urls.resolve("/webhooks/{webhook_id}/{token}", pathParams, Map.of());
        return http.request("PATCH", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> deleteWebhookWithToken(String webhookId, String token) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("webhook_id", webhookId);
        pathParams.put("token", token);
        String url = Urls.resolve("/webhooks/{webhook_id}/{token}", pathParams, Map.of());
        return http.request("DELETE", url, null, Map.of());
    }

    public CompletableFuture<String> executeWebhook(String webhookId, String token, Map<String, String> query, String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("webhook_id", webhookId);
        pathParams.put("token", token);
        String url = Urls.resolve("/webhooks/{webhook_id}/{token}", pathParams, query);
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> executeWebhookMultipart(String webhookId, String token, MultipartBody body) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("webhook_id", webhookId);
        pathParams.put("token", token);
        String url = Urls.resolve("/webhooks/{webhook_id}/{token}", pathParams, Map.of());
        return http.postMultipart(url, body, Map.of());
    }

    public CompletableFuture<String> executeGithubWebhook(String webhookId, String token, String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("webhook_id", webhookId);
        pathParams.put("token", token);
        String url = Urls.resolve("/webhooks/{webhook_id}/{token}/github", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> executeInstatusWebhook(String webhookId, String token, String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("webhook_id", webhookId);
        pathParams.put("token", token);
        String url = Urls.resolve("/webhooks/{webhook_id}/{token}/instatus", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> getWebhookMessage(String webhookId, String token, String messageId) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("webhook_id", webhookId);
        pathParams.put("token", token);
        pathParams.put("message_id", messageId);
        String url = Urls.resolve("/webhooks/{webhook_id}/{token}/messages/{message_id}", pathParams, Map.of());
        return http.request("GET", url, null, Map.of());
    }

    public CompletableFuture<String> editWebhookMessage(String webhookId, String token, String messageId, String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("webhook_id", webhookId);
        pathParams.put("token", token);
        pathParams.put("message_id", messageId);
        String url = Urls.resolve("/webhooks/{webhook_id}/{token}/messages/{message_id}", pathParams, Map.of());
        return http.request("PATCH", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> deleteWebhookMessage(String webhookId, String token, String messageId) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("webhook_id", webhookId);
        pathParams.put("token", token);
        pathParams.put("message_id", messageId);
        String url = Urls.resolve("/webhooks/{webhook_id}/{token}/messages/{message_id}", pathParams, Map.of());
        return http.request("DELETE", url, null, Map.of());
    }

    public CompletableFuture<String> executeSlackWebhook(String webhookId, String token, String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("webhook_id", webhookId);
        pathParams.put("token", token);
        String url = Urls.resolve("/webhooks/{webhook_id}/{token}/slack", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

}
