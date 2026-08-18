package flux.rest.api;

import flux.gateway.client.RestClient;
import flux.rest.Urls;
import okhttp3.MultipartBody;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public final class ChannelsApi {
    private final RestClient http;

    public ChannelsApi(RestClient http) {
        this.http = http;
    }

    public CompletableFuture<String> bulkListChannelMessages(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/channels/messages/bulk", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> getChannel(String channelId) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("channel_id", channelId);
        String url = Urls.resolve("/channels/{channel_id}", pathParams, Map.of());
        return http.request("GET", url, null, Map.of());
    }

    public CompletableFuture<String> updateChannel(String channelId, String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("channel_id", channelId);
        String url = Urls.resolve("/channels/{channel_id}", pathParams, Map.of());
        return http.request("PATCH", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> deleteChannel(String channelId, Map<String, String> query, String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("channel_id", channelId);
        String url = Urls.resolve("/channels/{channel_id}", pathParams, query);
        return http.request("DELETE", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> requestPresignedMessageAttachmentUploads(String channelId, String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("channel_id", channelId);
        String url = Urls.resolve("/channels/{channel_id}/attachments", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> completeMultipartMessageAttachmentUploads(String channelId, String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("channel_id", channelId);
        String url = Urls.resolve("/channels/{channel_id}/attachments/complete", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> getCallEligibility(String channelId) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("channel_id", channelId);
        String url = Urls.resolve("/channels/{channel_id}/call", pathParams, Map.of());
        return http.request("GET", url, null, Map.of());
    }

    public CompletableFuture<String> updateCallRegion(String channelId, String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("channel_id", channelId);
        String url = Urls.resolve("/channels/{channel_id}/call", pathParams, Map.of());
        return http.request("PATCH", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> endCall(String channelId) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("channel_id", channelId);
        String url = Urls.resolve("/channels/{channel_id}/call/end", pathParams, Map.of());
        return http.request("POST", url, null, Map.of());
    }

    public CompletableFuture<String> ringCallRecipients(String channelId, String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("channel_id", channelId);
        String url = Urls.resolve("/channels/{channel_id}/call/ring", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> stopRingingCallRecipients(String channelId, String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("channel_id", channelId);
        String url = Urls.resolve("/channels/{channel_id}/call/stop-ringing", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> listMessages(String channelId, Map<String, String> query) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("channel_id", channelId);
        String url = Urls.resolve("/channels/{channel_id}/messages", pathParams, query);
        return http.request("GET", url, null, Map.of());
    }

    public CompletableFuture<String> sendMessage(String channelId, String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("channel_id", channelId);
        String url = Urls.resolve("/channels/{channel_id}/messages", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> sendMessageMultipart(String channelId, MultipartBody body) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("channel_id", channelId);
        String url = Urls.resolve("/channels/{channel_id}/messages", pathParams, Map.of());
        return http.postMultipart(url, body, Map.of());
    }

    public CompletableFuture<String> clearChannelReadState(String channelId) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("channel_id", channelId);
        String url = Urls.resolve("/channels/{channel_id}/messages/ack", pathParams, Map.of());
        return http.request("DELETE", url, null, Map.of());
    }

    public CompletableFuture<String> bulkDeleteMessages(String channelId, String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("channel_id", channelId);
        String url = Urls.resolve("/channels/{channel_id}/messages/bulk-delete", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> bulkDeleteMyMessagesInChannel(String channelId, String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("channel_id", channelId);
        String url = Urls.resolve("/channels/{channel_id}/messages/bulk-delete-mine", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> listPinnedMessages(String channelId, Map<String, String> query) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("channel_id", channelId);
        String url = Urls.resolve("/channels/{channel_id}/messages/pins", pathParams, query);
        return http.request("GET", url, null, Map.of());
    }

    public CompletableFuture<String> purgePersonalNotesMessages(String channelId) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("channel_id", channelId);
        String url = Urls.resolve("/channels/{channel_id}/messages/purge", pathParams, Map.of());
        return http.request("POST", url, null, Map.of());
    }

    public CompletableFuture<String> scheduleMessage(String channelId, String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("channel_id", channelId);
        String url = Urls.resolve("/channels/{channel_id}/messages/schedule", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> scheduleMessageMultipart(String channelId, MultipartBody body) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("channel_id", channelId);
        String url = Urls.resolve("/channels/{channel_id}/messages/schedule", pathParams, Map.of());
        return http.postMultipart(url, body, Map.of());
    }

    public CompletableFuture<String> getMessage(String channelId, String messageId) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("channel_id", channelId);
        pathParams.put("message_id", messageId);
        String url = Urls.resolve("/channels/{channel_id}/messages/{message_id}", pathParams, Map.of());
        return http.request("GET", url, null, Map.of());
    }

    public CompletableFuture<String> editMessage(String channelId, String messageId, String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("channel_id", channelId);
        pathParams.put("message_id", messageId);
        String url = Urls.resolve("/channels/{channel_id}/messages/{message_id}", pathParams, Map.of());
        return http.request("PATCH", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> editMessageMultipart(String channelId, String messageId, MultipartBody body) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("channel_id", channelId);
        pathParams.put("message_id", messageId);
        String url = Urls.resolve("/channels/{channel_id}/messages/{message_id}", pathParams, Map.of());
        return http.postMultipart(url, body, Map.of());
    }

    public CompletableFuture<String> deleteMessage(String channelId, String messageId) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("channel_id", channelId);
        pathParams.put("message_id", messageId);
        String url = Urls.resolve("/channels/{channel_id}/messages/{message_id}", pathParams, Map.of());
        return http.request("DELETE", url, null, Map.of());
    }

    public CompletableFuture<String> acknowledgeMessage(String channelId, String messageId, String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("channel_id", channelId);
        pathParams.put("message_id", messageId);
        String url = Urls.resolve("/channels/{channel_id}/messages/{message_id}/ack", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> deleteMessageAttachment(String channelId, String messageId, String attachmentId) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("channel_id", channelId);
        pathParams.put("message_id", messageId);
        pathParams.put("attachment_id", attachmentId);
        String url = Urls.resolve("/channels/{channel_id}/messages/{message_id}/attachments/{attachment_id}", pathParams, Map.of());
        return http.request("DELETE", url, null, Map.of());
    }

    public CompletableFuture<String> removeAllReactions(String channelId, String messageId) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("channel_id", channelId);
        pathParams.put("message_id", messageId);
        String url = Urls.resolve("/channels/{channel_id}/messages/{message_id}/reactions", pathParams, Map.of());
        return http.request("DELETE", url, null, Map.of());
    }

    public CompletableFuture<String> listReactionUsers(String channelId, String messageId, String emoji, Map<String, String> query) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("channel_id", channelId);
        pathParams.put("message_id", messageId);
        pathParams.put("emoji", emoji);
        String url = Urls.resolve("/channels/{channel_id}/messages/{message_id}/reactions/{emoji}", pathParams, query);
        return http.request("GET", url, null, Map.of());
    }

    public CompletableFuture<String> removeAllReactionsForEmoji(String channelId, String messageId, String emoji) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("channel_id", channelId);
        pathParams.put("message_id", messageId);
        pathParams.put("emoji", emoji);
        String url = Urls.resolve("/channels/{channel_id}/messages/{message_id}/reactions/{emoji}", pathParams, Map.of());
        return http.request("DELETE", url, null, Map.of());
    }

    public CompletableFuture<String> addReaction(String channelId, String messageId, String emoji, Map<String, String> query) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("channel_id", channelId);
        pathParams.put("message_id", messageId);
        pathParams.put("emoji", emoji);
        String url = Urls.resolve("/channels/{channel_id}/messages/{message_id}/reactions/{emoji}/@me", pathParams, query);
        return http.request("PUT", url, null, Map.of());
    }

    public CompletableFuture<String> removeOwnReaction(String channelId, String messageId, String emoji, Map<String, String> query) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("channel_id", channelId);
        pathParams.put("message_id", messageId);
        pathParams.put("emoji", emoji);
        String url = Urls.resolve("/channels/{channel_id}/messages/{message_id}/reactions/{emoji}/@me", pathParams, query);
        return http.request("DELETE", url, null, Map.of());
    }

    public CompletableFuture<String> listReactionUsersV2(String channelId, String messageId, String emoji, Map<String, String> query) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("channel_id", channelId);
        pathParams.put("message_id", messageId);
        pathParams.put("emoji", emoji);
        String url = Urls.resolve("/channels/{channel_id}/messages/{message_id}/reactions/{emoji}/users", pathParams, query);
        return http.request("GET", url, null, Map.of());
    }

    public CompletableFuture<String> removeReaction(String channelId, String messageId, String emoji, String targetId, Map<String, String> query) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("channel_id", channelId);
        pathParams.put("message_id", messageId);
        pathParams.put("emoji", emoji);
        pathParams.put("target_id", targetId);
        String url = Urls.resolve("/channels/{channel_id}/messages/{message_id}/reactions/{emoji}/{target_id}", pathParams, query);
        return http.request("DELETE", url, null, Map.of());
    }

    public CompletableFuture<String> setChannelPermissionOverwrite(String channelId, String overwriteId, String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("channel_id", channelId);
        pathParams.put("overwrite_id", overwriteId);
        String url = Urls.resolve("/channels/{channel_id}/permissions/{overwrite_id}", pathParams, Map.of());
        return http.request("PUT", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> deleteChannelPermissionOverwrite(String channelId, String overwriteId) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("channel_id", channelId);
        pathParams.put("overwrite_id", overwriteId);
        String url = Urls.resolve("/channels/{channel_id}/permissions/{overwrite_id}", pathParams, Map.of());
        return http.request("DELETE", url, null, Map.of());
    }

    public CompletableFuture<String> acknowledgePins(String channelId) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("channel_id", channelId);
        String url = Urls.resolve("/channels/{channel_id}/pins/ack", pathParams, Map.of());
        return http.request("POST", url, null, Map.of());
    }

    public CompletableFuture<String> pinMessage(String channelId, String messageId) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("channel_id", channelId);
        pathParams.put("message_id", messageId);
        String url = Urls.resolve("/channels/{channel_id}/pins/{message_id}", pathParams, Map.of());
        return http.request("PUT", url, null, Map.of());
    }

    public CompletableFuture<String> unpinMessage(String channelId, String messageId) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("channel_id", channelId);
        pathParams.put("message_id", messageId);
        String url = Urls.resolve("/channels/{channel_id}/pins/{message_id}", pathParams, Map.of());
        return http.request("DELETE", url, null, Map.of());
    }

    public CompletableFuture<String> addGroupDmRecipient(String channelId, String userId) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("channel_id", channelId);
        pathParams.put("user_id", userId);
        String url = Urls.resolve("/channels/{channel_id}/recipients/{user_id}", pathParams, Map.of());
        return http.request("PUT", url, null, Map.of());
    }

    public CompletableFuture<String> removeGroupDmRecipient(String channelId, String userId, Map<String, String> query, String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("channel_id", channelId);
        pathParams.put("user_id", userId);
        String url = Urls.resolve("/channels/{channel_id}/recipients/{user_id}", pathParams, query);
        return http.request("DELETE", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> listRtcRegions(String channelId) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("channel_id", channelId);
        String url = Urls.resolve("/channels/{channel_id}/rtc-regions", pathParams, Map.of());
        return http.request("GET", url, null, Map.of());
    }

    public CompletableFuture<String> getChannelSlowmodeState(String channelId) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("channel_id", channelId);
        String url = Urls.resolve("/channels/{channel_id}/slowmode", pathParams, Map.of());
        return http.request("GET", url, null, Map.of());
    }

    public CompletableFuture<String> indicateTyping(String channelId) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("channel_id", channelId);
        String url = Urls.resolve("/channels/{channel_id}/typing", pathParams, Map.of());
        return http.request("POST", url, null, Map.of());
    }

    public CompletableFuture<String> uploadVoiceDebugLoggingEvents(String channelId, String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("channel_id", channelId);
        String url = Urls.resolve("/channels/{channel_id}/voice-debug-logging/events", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> getVoiceDebugLoggingStatus(String channelId) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("channel_id", channelId);
        String url = Urls.resolve("/channels/{channel_id}/voice-debug-logging/session", pathParams, Map.of());
        return http.request("GET", url, null, Map.of());
    }

    public CompletableFuture<String> setVoiceDebugLoggingStatus(String channelId, String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("channel_id", channelId);
        String url = Urls.resolve("/channels/{channel_id}/voice-debug-logging/session", pathParams, Map.of());
        return http.request("PUT", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> heartbeatVoicePresence(String channelId, String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("channel_id", channelId);
        String url = Urls.resolve("/channels/{channel_id}/voice-presence/heartbeat", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> endVoicePresenceHeartbeat(String channelId, String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("channel_id", channelId);
        String url = Urls.resolve("/channels/{channel_id}/voice-presence/heartbeat", pathParams, Map.of());
        return http.request("DELETE", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> getStreamPreview(String streamKey) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("stream_key", streamKey);
        String url = Urls.resolve("/streams/{stream_key}/preview", pathParams, Map.of());
        return http.request("GET", url, null, Map.of());
    }

    public CompletableFuture<String> uploadStreamPreview(String streamKey, String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("stream_key", streamKey);
        String url = Urls.resolve("/streams/{stream_key}/preview", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> deleteStreamPreview(String streamKey) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("stream_key", streamKey);
        String url = Urls.resolve("/streams/{stream_key}/preview", pathParams, Map.of());
        return http.request("DELETE", url, null, Map.of());
    }

    public CompletableFuture<String> createStreamPreviewUploadUrl(String streamKey, String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("stream_key", streamKey);
        String url = Urls.resolve("/streams/{stream_key}/preview/upload-url", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> updateStreamRegion(String streamKey, String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("stream_key", streamKey);
        String url = Urls.resolve("/streams/{stream_key}/stream", pathParams, Map.of());
        return http.request("PATCH", url, jsonBody, Map.of());
    }

}
