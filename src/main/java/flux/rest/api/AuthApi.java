package flux.rest.api;

import flux.gateway.client.RestClient;
import flux.rest.Urls;
import okhttp3.MultipartBody;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public final class AuthApi {
    private final RestClient http;

    public AuthApi(RestClient http) {
        this.http = http;
    }

    public CompletableFuture<String> authorizeIpAddress(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/auth/authorize-ip", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> revertEmailChange(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/auth/email-revert", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> forgotPassword(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/auth/forgot", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> completeHandoff(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/auth/handoff/complete", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> initiateHandoff() {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/auth/handoff/initiate", pathParams, Map.of());
        return http.request("POST", url, null, Map.of());
    }

    public CompletableFuture<String> cancelHandoff(String code) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("code", code);
        String url = Urls.resolve("/auth/handoff/{code}", pathParams, Map.of());
        return http.request("DELETE", url, null, Map.of());
    }

    public CompletableFuture<String> getHandoffInfo(String code) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("code", code);
        String url = Urls.resolve("/auth/handoff/{code}/info", pathParams, Map.of());
        return http.request("GET", url, null, Map.of());
    }

    public CompletableFuture<String> getHandoffStatus(String code) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("code", code);
        String url = Urls.resolve("/auth/handoff/{code}/status", pathParams, Map.of());
        return http.request("GET", url, null, Map.of());
    }

    public CompletableFuture<String> pollIpAuthorization(Map<String, String> query) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/auth/ip-authorization/poll", pathParams, query);
        return http.request("GET", url, null, Map.of());
    }

    public CompletableFuture<String> resendIpAuthorization(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/auth/ip-authorization/resend", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> loginUser(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/auth/login", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> loginWithTotp(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/auth/login/mfa/totp", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> loginWithWebauthnMfa(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/auth/login/mfa/webauthn", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> getWebauthnMfaOptions(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/auth/login/mfa/webauthn/authentication-options", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> logoutUser() {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/auth/logout", pathParams, Map.of());
        return http.request("POST", url, null, Map.of());
    }

    public CompletableFuture<String> registerAccount(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/auth/register", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> resetPassword(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/auth/reset", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> validateResetPasswordToken(String token) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        pathParams.put("token", token);
        String url = Urls.resolve("/auth/reset/{token}", pathParams, Map.of());
        return http.request("GET", url, null, Map.of());
    }

    public CompletableFuture<String> listAuthSessions() {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/auth/sessions", pathParams, Map.of());
        return http.request("GET", url, null, Map.of());
    }

    public CompletableFuture<String> logoutAllSessions(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/auth/sessions/logout", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> completeSso(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/auth/sso/complete", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> startSso(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/auth/sso/start", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> getSsoStatus() {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/auth/sso/status", pathParams, Map.of());
        return http.request("GET", url, null, Map.of());
    }

    public CompletableFuture<String> getUsernameSuggestions(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/auth/username-suggestions", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> verifyEmail(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/auth/verify", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> resendVerificationEmail() {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/auth/verify/resend", pathParams, Map.of());
        return http.request("POST", url, null, Map.of());
    }

    public CompletableFuture<String> authenticateWithWebauthn(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/auth/webauthn/authenticate", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> getWebauthnAuthenticationOptions() {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/auth/webauthn/authentication-options", pathParams, Map.of());
        return http.request("POST", url, null, Map.of());
    }

}
