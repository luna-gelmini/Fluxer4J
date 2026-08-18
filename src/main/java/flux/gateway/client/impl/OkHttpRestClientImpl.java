package flux.gateway.client.impl;

import flux.api.exception.FluxException;
import flux.gateway.client.RestClient;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class OkHttpRestClientImpl implements RestClient {

    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private final OkHttpClient httpClient;
    private String botToken;

    public OkHttpRestClientImpl() {
        this(new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build());
    }

    public OkHttpRestClientImpl(OkHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public void setBotToken(String botToken) {
        this.botToken = Objects.requireNonNull(botToken, "Bot token cannot be null");
    }

    private Map<String, String> ensureAuthHeader(Map<String, String> headers) {
        if (botToken == null) {
            return headers;
        }
        Map<String, String> newHeaders = new HashMap<>(headers);
        newHeaders.putIfAbsent("Authorization", "Bot " + botToken);
        return newHeaders;
    }

    @Override
    public CompletableFuture<String> get(String url, Map<String, String> headers) {
        return executeRequest(new Request.Builder()
                .url(url)
                .get()
                .headers(Headers.of(ensureAuthHeader(headers)))
                .build());
    }

    @Override
    public CompletableFuture<String> post(String url, String jsonPayload, Map<String, String> headers) {
        return executeRequest(new Request.Builder()
                .url(url)
                .post(RequestBody.create(jsonPayload == null ? "{}" : jsonPayload, JSON))
                .headers(Headers.of(ensureAuthHeader(headers)))
                .build());
    }

    @Override
    public CompletableFuture<String> postMultipart(String url, MultipartBody body, Map<String, String> headers) {
        return executeRequest(new Request.Builder()
                .url(url)
                .post(body)
                .headers(Headers.of(ensureAuthHeader(headers)))
                .build());
    }

    @Override
    public CompletableFuture<String> put(String url, String jsonPayload, Map<String, String> headers) {
        RequestBody body = jsonPayload != null
                ? RequestBody.create(jsonPayload, JSON)
                : RequestBody.create("{}", JSON);
        return executeRequest(new Request.Builder()
                .url(url)
                .put(body)
                .headers(Headers.of(ensureAuthHeader(headers)))
                .build());
    }

    @Override
    public CompletableFuture<String> patch(String url, String jsonPayload, Map<String, String> headers) {
        return executeRequest(new Request.Builder()
                .url(url)
                .patch(RequestBody.create(jsonPayload == null ? "{}" : jsonPayload, JSON))
                .headers(Headers.of(ensureAuthHeader(headers)))
                .build());
    }

    @Override
    public CompletableFuture<String> delete(String url, Map<String, String> headers) {
        return executeRequest(new Request.Builder()
                .url(url)
                .delete()
                .headers(Headers.of(ensureAuthHeader(headers)))
                .build());
    }

    @Override
    public CompletableFuture<String> request(String method, String url, String jsonPayload, Map<String, String> headers) {
        String verb = method.toUpperCase(java.util.Locale.ROOT);
        boolean noBody = "GET".equals(verb) || "HEAD".equals(verb)
                || ("DELETE".equals(verb) && (jsonPayload == null || jsonPayload.isBlank()));
        RequestBody body = noBody ? null : RequestBody.create(
                jsonPayload == null || jsonPayload.isBlank() ? "{}" : jsonPayload, JSON);
        return executeRequest(new Request.Builder()
                .url(url)
                .method(verb, body)
                .headers(Headers.of(ensureAuthHeader(headers)))
                .build());
    }

    @Override
    public void shutdown() {
        httpClient.dispatcher().executorService().shutdown();
        httpClient.connectionPool().evictAll();
    }

    private CompletableFuture<String> executeRequest(Request request) {
        CompletableFuture<String> future = new CompletableFuture<>();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                future.completeExceptionally(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try (ResponseBody body = response.body()) {
                    String bodyString = body != null ? body.string() : "";
                    if (!response.isSuccessful()) {
                        future.completeExceptionally(new FluxException(
                                "Request failed: " + response.code() + " " + response.message() + " - " + bodyString));
                    } else {
                        future.complete(bodyString);
                    }
                }
            }
        });
        return future;
    }
}
