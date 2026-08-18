package flux.rest.api;

import flux.gateway.client.RestClient;
import flux.rest.Urls;
import okhttp3.MultipartBody;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public final class UsersApi {
    private final RestClient http;

    public UsersApi(RestClient http) {
        this.http = http;
    }

    public CompletableFuture<String> getCurrentUser() {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/users/@me", pathParams, Map.of());
        return http.request("GET", url, null, Map.of());
    }

    public CompletableFuture<String> updateCurrentUser(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/users/@me", pathParams, Map.of());
        return http.request("PATCH", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> forgetAuthorizedIps(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/users/@me/authorized-ips", pathParams, Map.of());
        return http.request("DELETE", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> listPrivateChannels() {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/users/@me/channels", pathParams, Map.of());
        return http.request("GET", url, null, Map.of());
    }

    public CompletableFuture<String> createPrivateChannel(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/users/@me/channels", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> preloadMessagesForChannelsAlt(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/users/@me/channels/messages/preload", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> pinDirectMessageChannel(String channelId) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("channel_id", channelId);
        String url = Urls.resolve("/users/@me/channels/{channel_id}/pin", pathParams, Map.of());
        return http.request("PUT", url, null, Map.of());
    }

    public CompletableFuture<String> unpinDirectMessageChannel(String channelId) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("channel_id", channelId);
        String url = Urls.resolve("/users/@me/channels/{channel_id}/pin", pathParams, Map.of());
        return http.request("DELETE", url, null, Map.of());
    }

    public CompletableFuture<String> deleteCurrentUserAccount(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/users/@me/delete", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> disableCurrentUserAccount(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/users/@me/disable", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> applyEmailChange(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/users/@me/email-change/apply", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> requestBouncedEmailReplacement(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/users/@me/email-change/bounced/request-new", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> resendBouncedEmailReplacementCode(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/users/@me/email-change/bounced/resend-new", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> verifyBouncedEmailReplacement(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/users/@me/email-change/bounced/verify-new", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> requestNewEmailAddress(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/users/@me/email-change/request-new", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> resendNewEmailConfirmation(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/users/@me/email-change/resend-new", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> resendOriginalEmailConfirmation(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/users/@me/email-change/resend-original", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> startEmailChange(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/users/@me/email-change/start", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> verifyNewEmailAddress(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/users/@me/email-change/verify-new", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> verifyOriginalEmailAddress(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/users/@me/email-change/verify-original", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> setEntranceSoundSelection(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/users/@me/entrance-sound-selections", pathParams, Map.of());
        return http.request("PUT", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> listEntranceSounds() {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/users/@me/entrance-sounds", pathParams, Map.of());
        return http.request("GET", url, null, Map.of());
    }

    public CompletableFuture<String> uploadEntranceSound(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/users/@me/entrance-sounds", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> renameEntranceSound(String soundId, String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("sound_id", soundId);
        String url = Urls.resolve("/users/@me/entrance-sounds/{sound_id}", pathParams, Map.of());
        return http.request("PATCH", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> deleteEntranceSound(String soundId) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("sound_id", soundId);
        String url = Urls.resolve("/users/@me/entrance-sounds/{sound_id}", pathParams, Map.of());
        return http.request("DELETE", url, null, Map.of());
    }

    public CompletableFuture<String> listUserGifts() {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/users/@me/gifts", pathParams, Map.of());
        return http.request("GET", url, null, Map.of());
    }

    public CompletableFuture<String> updateDmNotificationSettings(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/users/@me/guilds/@me/settings", pathParams, Map.of());
        return http.request("PATCH", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> updateGuildSettingsForUser(String guildId, String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("guild_id", guildId);
        String url = Urls.resolve("/users/@me/guilds/{guild_id}/settings", pathParams, Map.of());
        return http.request("PATCH", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> requestDataHarvest() {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/users/@me/harvest", pathParams, Map.of());
        return http.request("POST", url, null, Map.of());
    }

    public CompletableFuture<String> requestFilteredDataHarvest(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/users/@me/harvest/filtered", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> getLatestDataHarvest() {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/users/@me/harvest/latest", pathParams, Map.of());
        return http.request("GET", url, null, Map.of());
    }

    public CompletableFuture<String> getDataHarvestStatus(String harvestId) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("harvestId", harvestId);
        String url = Urls.resolve("/users/@me/harvest/{harvestId}", pathParams, Map.of());
        return http.request("GET", url, null, Map.of());
    }

    public CompletableFuture<String> getDataHarvestDownloadUrl(String harvestId) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("harvestId", harvestId);
        String url = Urls.resolve("/users/@me/harvest/{harvestId}/download", pathParams, Map.of());
        return http.request("GET", url, null, Map.of());
    }

    public CompletableFuture<String> listMentionsForCurrentUser(Map<String, String> query) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/users/@me/mentions", pathParams, query);
        return http.request("GET", url, null, Map.of());
    }

    public CompletableFuture<String> markMentionsRead(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/users/@me/mentions/read", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> deleteMention(String messageId) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("message_id", messageId);
        String url = Urls.resolve("/users/@me/mentions/{message_id}", pathParams, Map.of());
        return http.request("DELETE", url, null, Map.of());
    }

    public CompletableFuture<String> bulkDeleteMyMessages(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/users/@me/messages/bulk-delete-mine", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> requestBulkMessageDeletion(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/users/@me/messages/delete", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> cancelBulkMessageDeletion() {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/users/@me/messages/delete", pathParams, Map.of());
        return http.request("DELETE", url, null, Map.of());
    }

    public CompletableFuture<String> getBackupCodesMfa(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/users/@me/mfa/backup-codes", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> disableTotpMfa(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/users/@me/mfa/totp/disable", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> enableTotpMfa(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/users/@me/mfa/totp/enable", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> listWebauthnCredentials() {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/users/@me/mfa/webauthn/credentials", pathParams, Map.of());
        return http.request("GET", url, null, Map.of());
    }

    public CompletableFuture<String> registerWebauthnCredential(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/users/@me/mfa/webauthn/credentials", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> getWebauthnRegistrationOptions(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/users/@me/mfa/webauthn/credentials/registration-options", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> updateWebauthnCredential(String credentialId, String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("credential_id", credentialId);
        String url = Urls.resolve("/users/@me/mfa/webauthn/credentials/{credential_id}", pathParams, Map.of());
        return http.request("PATCH", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> deleteWebauthnCredential(String credentialId, String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("credential_id", credentialId);
        String url = Urls.resolve("/users/@me/mfa/webauthn/credentials/{credential_id}", pathParams, Map.of());
        return http.request("DELETE", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> registerMobilePushDevice(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/users/@me/mobile-devices", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> listMobilePushDevices() {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/users/@me/mobile-devices", pathParams, Map.of());
        return http.request("GET", url, null, Map.of());
    }

    public CompletableFuture<String> unregisterMobilePushDevice(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/users/@me/mobile-devices/unregister", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> deleteMobilePushDevice(String deviceId) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("device_id", deviceId);
        String url = Urls.resolve("/users/@me/mobile-devices/{device_id}", pathParams, Map.of());
        return http.request("DELETE", url, null, Map.of());
    }

    public CompletableFuture<String> listCurrentUserNotes() {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/users/@me/notes", pathParams, Map.of());
        return http.request("GET", url, null, Map.of());
    }

    public CompletableFuture<String> getNoteOnUser(String targetId) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("target_id", targetId);
        String url = Urls.resolve("/users/@me/notes/{target_id}", pathParams, Map.of());
        return http.request("GET", url, null, Map.of());
    }

    public CompletableFuture<String> setNoteOnUser(String targetId, String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("target_id", targetId);
        String url = Urls.resolve("/users/@me/notes/{target_id}", pathParams, Map.of());
        return http.request("PUT", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> completePasswordChange(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/users/@me/password-change/complete", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> resendPasswordChangeCode(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/users/@me/password-change/resend", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> startPasswordChange(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/users/@me/password-change/start", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> verifyPasswordChangeCode(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/users/@me/password-change/verify", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> startInboundPhoneChallenge() {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/users/@me/phone/inbound-challenge", pathParams, Map.of());
        return http.request("POST", url, null, Map.of());
    }

    public CompletableFuture<String> sendPhoneVerificationCode(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/users/@me/phone/send-verification", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> verifyPhoneCode(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/users/@me/phone/verify", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> preloadMessagesForChannels(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/users/@me/preload-messages", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> resetCurrentUserPremiumState() {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/users/@me/premium/reset", pathParams, Map.of());
        return http.request("POST", url, null, Map.of());
    }

    public CompletableFuture<String> rotatePushSubscription(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/users/@me/push/rotate", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> subscribeToPushNotifications(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/users/@me/push/subscribe", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> listPushSubscriptions() {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/users/@me/push/subscriptions", pathParams, Map.of());
        return http.request("GET", url, null, Map.of());
    }

    public CompletableFuture<String> unsubscribeFromPushNotifications(String subscriptionId) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("subscription_id", subscriptionId);
        String url = Urls.resolve("/users/@me/push/subscriptions/{subscription_id}", pathParams, Map.of());
        return http.request("DELETE", url, null, Map.of());
    }

    public CompletableFuture<String> listUserRelationships() {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/users/@me/relationships", pathParams, Map.of());
        return http.request("GET", url, null, Map.of());
    }

    public CompletableFuture<String> sendFriendRequestByTag(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/users/@me/relationships", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> bulkIgnoreFriendRequests(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/users/@me/relationships/bulk-ignore", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> sendFriendRequest(String userId, String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("user_id", userId);
        String url = Urls.resolve("/users/@me/relationships/{user_id}", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> acceptOrUpdateFriendRequest(String userId, String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("user_id", userId);
        String url = Urls.resolve("/users/@me/relationships/{user_id}", pathParams, Map.of());
        return http.request("PUT", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> removeRelationship(String userId) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("user_id", userId);
        String url = Urls.resolve("/users/@me/relationships/{user_id}", pathParams, Map.of());
        return http.request("DELETE", url, null, Map.of());
    }

    public CompletableFuture<String> updateRelationshipNickname(String userId, String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("user_id", userId);
        String url = Urls.resolve("/users/@me/relationships/{user_id}", pathParams, Map.of());
        return http.request("PATCH", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> listSavedMessages(Map<String, String> query) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/users/@me/saved-messages", pathParams, query);
        return http.request("GET", url, null, Map.of());
    }

    public CompletableFuture<String> saveMessage(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/users/@me/saved-messages", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> unsaveMessage(String messageId) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("message_id", messageId);
        String url = Urls.resolve("/users/@me/saved-messages/{message_id}", pathParams, Map.of());
        return http.request("DELETE", url, null, Map.of());
    }

    public CompletableFuture<String> listScheduledMessages() {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/users/@me/scheduled-messages", pathParams, Map.of());
        return http.request("GET", url, null, Map.of());
    }

    public CompletableFuture<String> getScheduledMessage(String scheduledMessageId) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("scheduled_message_id", scheduledMessageId);
        String url = Urls.resolve("/users/@me/scheduled-messages/{scheduled_message_id}", pathParams, Map.of());
        return http.request("GET", url, null, Map.of());
    }

    public CompletableFuture<String> cancelScheduledMessage(String scheduledMessageId) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("scheduled_message_id", scheduledMessageId);
        String url = Urls.resolve("/users/@me/scheduled-messages/{scheduled_message_id}", pathParams, Map.of());
        return http.request("DELETE", url, null, Map.of());
    }

    public CompletableFuture<String> updateScheduledMessage(String scheduledMessageId, String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("scheduled_message_id", scheduledMessageId);
        String url = Urls.resolve("/users/@me/scheduled-messages/{scheduled_message_id}", pathParams, Map.of());
        return http.request("PATCH", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> updateScheduledMessageMultipart(String scheduledMessageId, MultipartBody body) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("scheduled_message_id", scheduledMessageId);
        String url = Urls.resolve("/users/@me/scheduled-messages/{scheduled_message_id}", pathParams, Map.of());
        return http.postMultipart(url, body, Map.of());
    }

    public CompletableFuture<String> getCurrentUserSettings() {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/users/@me/settings", pathParams, Map.of());
        return http.request("GET", url, null, Map.of());
    }

    public CompletableFuture<String> updateCurrentUserSettings(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/users/@me/settings", pathParams, Map.of());
        return http.request("PATCH", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> updateVoiceActivitySharingDefault(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/users/@me/settings/voice-activity-sharing", pathParams, Map.of());
        return http.request("PUT", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> listSudoMfaMethods() {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/users/@me/sudo/mfa-methods", pathParams, Map.of());
        return http.request("GET", url, null, Map.of());
    }

    public CompletableFuture<String> getSudoWebauthnAuthenticationOptions() {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/users/@me/sudo/webauthn/authentication-options", pathParams, Map.of());
        return http.request("POST", url, null, Map.of());
    }

    public CompletableFuture<String> acceptUpdatedTerms(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/users/@me/terms-acceptance", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> checkUsernameTagAvailability(Map<String, String> query) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/users/check-tag", pathParams, query);
        return http.request("GET", url, null, Map.of());
    }

    public CompletableFuture<String> getUserProfile(String targetId, Map<String, String> query) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("target_id", targetId);
        String url = Urls.resolve("/users/{target_id}/profile", pathParams, query);
        return http.request("GET", url, null, Map.of());
    }

    public CompletableFuture<String> getUserById(String userId) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("user_id", userId);
        String url = Urls.resolve("/users/{user_id}", pathParams, Map.of());
        return http.request("GET", url, null, Map.of());
    }

}
