package flux.rest.api;

import flux.gateway.client.RestClient;
import flux.rest.Urls;
import okhttp3.MultipartBody;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public final class BillingApi {
    private final RestClient http;

    public BillingApi(RestClient http) {
        this.http = http;
    }

    public CompletableFuture<String> getSelfServeRefundEligibility() {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/premium/refund-eligibility", pathParams, Map.of());
        return http.request("GET", url, null, Map.of());
    }

    public CompletableFuture<String> selfServeRefundLatestPurchase() {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/premium/refund-latest", pathParams, Map.of());
        return http.request("POST", url, null, Map.of());
    }

    public CompletableFuture<String> createGiftCheckoutSession(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/stripe/checkout/gift", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> createCheckoutSession(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/stripe/checkout/subscription", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> createLocalizedCardPreapprovalSession(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/stripe/checkout/subscription/preapproval", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> continueLocalizedCardPreapprovalSession(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/stripe/checkout/subscription/preapproval/continue", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> processStripeWebhook() {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/stripe/webhook", pathParams, Map.of());
        return http.request("POST", url, null, Map.of());
    }

    public CompletableFuture<String> createAgeVerificationSession() {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/users/@me/age-verification", pathParams, Map.of());
        return http.request("POST", url, null, Map.of());
    }

}
