package flux.rest.api;

import flux.gateway.client.RestClient;
import flux.rest.Urls;
import okhttp3.MultipartBody;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public final class Oauth2Api {
    private final RestClient http;

    public Oauth2Api(RestClient http) {
        this.http = http;
    }

    public CompletableFuture<String> getCurrentUserApplications() {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/applications/@me", pathParams, Map.of());
        return http.request("GET", url, null, Map.of());
    }

    public CompletableFuture<String> getCurrentUserOauth2() {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/oauth2/@me", pathParams, Map.of());
        return http.request("GET", url, null, Map.of());
    }

    public CompletableFuture<String> listUserOauth2Authorizations() {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/oauth2/@me/authorizations", pathParams, Map.of());
        return http.request("GET", url, null, Map.of());
    }

    public CompletableFuture<String> bulkDeleteUserOauth2Authorizations(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/oauth2/@me/authorizations/revoke", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> deleteUserOauth2Authorization(String applicationId) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("applicationId", applicationId);
        String url = Urls.resolve("/oauth2/@me/authorizations/{applicationId}", pathParams, Map.of());
        return http.request("DELETE", url, null, Map.of());
    }

    public CompletableFuture<String> createOauthApplication(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/oauth2/applications", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> getOauthApplicationsMe() {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/oauth2/applications/@me", pathParams, Map.of());
        return http.request("GET", url, null, Map.of());
    }

    public CompletableFuture<String> getOauthApplication(String id) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("id", id);
        String url = Urls.resolve("/oauth2/applications/{id}", pathParams, Map.of());
        return http.request("GET", url, null, Map.of());
    }

    public CompletableFuture<String> updateOauthApplication(String id, String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("id", id);
        String url = Urls.resolve("/oauth2/applications/{id}", pathParams, Map.of());
        return http.request("PATCH", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> deleteOauthApplication(String id, String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("id", id);
        String url = Urls.resolve("/oauth2/applications/{id}", pathParams, Map.of());
        return http.request("DELETE", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> updateBotProfile(String id, String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("id", id);
        String url = Urls.resolve("/oauth2/applications/{id}/bot", pathParams, Map.of());
        return http.request("PATCH", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> resetBotToken(String id, String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("id", id);
        String url = Urls.resolve("/oauth2/applications/{id}/bot/reset-token", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> resetClientSecret(String id, String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("id", id);
        String url = Urls.resolve("/oauth2/applications/{id}/client-secret/reset", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> getPublicApplication(String id) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("id", id);
        String url = Urls.resolve("/oauth2/applications/{id}/public", pathParams, Map.of());
        return http.request("GET", url, null, Map.of());
    }

    public CompletableFuture<String> provideOauth2Consent(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/oauth2/authorize/consent", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> introspectOauth2Token() {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/oauth2/introspect", pathParams, Map.of());
        return http.request("POST", url, null, Map.of());
    }

    public CompletableFuture<String> introspectOauth2TokenMultipart(MultipartBody body) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/oauth2/introspect", pathParams, Map.of());
        return http.postMultipart(url, body, Map.of());
    }

    public CompletableFuture<String> exchangeOauth2Token() {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/oauth2/token", pathParams, Map.of());
        return http.request("POST", url, null, Map.of());
    }

    public CompletableFuture<String> exchangeOauth2TokenMultipart(MultipartBody body) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/oauth2/token", pathParams, Map.of());
        return http.postMultipart(url, body, Map.of());
    }

    public CompletableFuture<String> revokeOauth2Token() {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/oauth2/token/revoke", pathParams, Map.of());
        return http.request("POST", url, null, Map.of());
    }

    public CompletableFuture<String> revokeOauth2TokenMultipart(MultipartBody body) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/oauth2/token/revoke", pathParams, Map.of());
        return http.postMultipart(url, body, Map.of());
    }

    public CompletableFuture<String> getOauth2Userinfo() {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/oauth2/userinfo", pathParams, Map.of());
        return http.request("GET", url, null, Map.of());
    }

    public CompletableFuture<String> listUserApplications() {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/users/@me/applications", pathParams, Map.of());
        return http.request("GET", url, null, Map.of());
    }

}
