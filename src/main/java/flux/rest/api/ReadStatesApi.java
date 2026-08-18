package flux.rest.api;

import flux.gateway.client.RestClient;
import flux.rest.Urls;
import okhttp3.MultipartBody;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public final class ReadStatesApi {
    private final RestClient http;

    public ReadStatesApi(RestClient http) {
        this.http = http;
    }

    public CompletableFuture<String> ackReadStates(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/read-states/ack", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

    public CompletableFuture<String> ackBulkMessages(String jsonBody) {
        Map<String, String> pathParams = new LinkedHashMap<>();
        String url = Urls.resolve("/read-states/ack-bulk", pathParams, Map.of());
        return http.request("POST", url, jsonBody, Map.of());
    }

}
