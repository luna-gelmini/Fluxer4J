package flux.rest.api;

import flux.gateway.client.RestClient;
import flux.rest.Urls;
import okhttp3.MultipartBody;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public final class ReportsApi {
    private final RestClient http;

    public ReportsApi(RestClient http) {
        this.http = http;
    }

    public CompletableFuture<String> createDsaReport(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/reports/dsa", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> sendDsaReportEmail(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/reports/dsa/email/send", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> verifyDsaReportEmail(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/reports/dsa/email/verify", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> reportGuild(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/reports/guild", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> reportMessage(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/reports/message", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> reportUser(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/reports/user", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

}
