package flux.gateway.client;

import okhttp3.MultipartBody;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface RestClient {

    void setBotToken(String botToken);

    CompletableFuture<String> get(String url, Map<String, String> headers);

    CompletableFuture<String> patch(String url, String jsonPayload, Map<String, String> headers);

    CompletableFuture<String> post(String url, String jsonPayload, Map<String, String> headers);

    CompletableFuture<String> postMultipart(String url, MultipartBody body, Map<String, String> headers);

    CompletableFuture<String> put(String url, String jsonPayload, Map<String, String> headers);

    CompletableFuture<String> delete(String url, Map<String, String> headers);

    CompletableFuture<String> request(String method, String url, String jsonPayload, Map<String, String> headers);

    void shutdown();
}
