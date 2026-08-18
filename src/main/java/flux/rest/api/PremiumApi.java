package flux.rest.api;

import flux.gateway.client.RestClient;
import flux.rest.Urls;
import okhttp3.MultipartBody;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public final class PremiumApi {
    private final RestClient http;

    public PremiumApi(RestClient http) {
        this.http = http;
    }

    public CompletableFuture<String> cancelPendingSubscriptionChange() {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/premium/cancel-pending-subscription-change", pathParams, Map.of());
        return http.request("POST", url, null, Map.of());
    }

    public CompletableFuture<String> cancelSubscription() {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/premium/cancel-subscription", pathParams, Map.of());
        return http.request("POST", url, null, Map.of());
    }

    public CompletableFuture<String> changeSubscriptionBillingCycle(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/premium/change-subscription", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> getCurrentSubscriptionPrice() {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/premium/current-subscription-price", pathParams, Map.of());
        return http.request("GET", url, null, Map.of());
    }

    public CompletableFuture<String> createCustomerPortal() {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/premium/customer-portal", pathParams, Map.of());
        return http.request("POST", url, null, Map.of());
    }

    public CompletableFuture<String> endPremiumGracePeriod() {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/premium/grace/end", pathParams, Map.of());
        return http.request("POST", url, null, Map.of());
    }

    public CompletableFuture<String> setPremiumPerksDisabled(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/premium/perks-disabled", pathParams, Map.of());
        return http.request("PATCH", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> getPriceIds(Map<String, String> query) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/premium/price-ids", pathParams, query);
        return http.request("GET", url, null, Map.of());
    }

    public CompletableFuture<String> reactivateSubscription() {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/premium/reactivate-subscription", pathParams, Map.of());
        return http.request("POST", url, null, Map.of());
    }

    public CompletableFuture<String> getPremiumState(Map<String, String> query) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/premium/state", pathParams, query);
        return http.request("GET", url, null, Map.of());
    }

    public CompletableFuture<String> rejoinVisionaryGuild() {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/premium/visionary/rejoin", pathParams, Map.of());
        return http.request("POST", url, null, Map.of());
    }

}
