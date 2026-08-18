package flux.rest.api;

import flux.gateway.client.RestClient;
import flux.rest.Urls;
import okhttp3.MultipartBody;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public final class DonationsApi {
    private final RestClient http;

    public DonationsApi(RestClient http) {
        this.http = http;
    }

    public CompletableFuture<String> createDonationCheckout(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/donations/checkout", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> manageDonation(Map<String, String> query) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/donations/manage", pathParams, query);
        return http.request("GET", url, null, Map.of());
    }

    public CompletableFuture<String> requestDonationMagicLink(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/donations/request-link", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

}
