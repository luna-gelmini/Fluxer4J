package flux.rest;

import flux.api.ApiEnvironment;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.StringJoiner;

public final class Urls {
    private Urls() {
    }

    public static String resolve(String pathTemplate, Map<String, String> pathParams, Map<String, String> query) {
        String path = pathTemplate;
        if (pathParams != null) {
            for (Map.Entry<String, String> entry : pathParams.entrySet()) {
                String value = entry.getValue() == null ? "" : URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8)
                        .replace("+", "%20");
                path = path.replace("{" + entry.getKey() + "}", value);
            }
        }
        String base = ApiEnvironment.getApiBaseUrl();
        if (path.startsWith("/.well-known")) {
            base = origin(base);
        }
        StringBuilder url = new StringBuilder(base).append(path);
        if (query != null && !query.isEmpty()) {
            StringJoiner joiner = new StringJoiner("&");
            for (Map.Entry<String, String> entry : query.entrySet()) {
                if (entry.getValue() == null || entry.getValue().isBlank()) {
                    continue;
                }
                joiner.add(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8)
                        + "="
                        + URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
            }
            String encoded = joiner.toString();
            if (!encoded.isEmpty()) {
                url.append(path.contains("?") ? "&" : "?").append(encoded);
            }
        }
        return url.toString();
    }

    static String origin(String apiBase) {
        String base = apiBase.endsWith("/") ? apiBase.substring(0, apiBase.length() - 1) : apiBase;
        if (base.endsWith("/v1")) {
            return base.substring(0, base.length() - 3);
        }
        return base;
    }
}
