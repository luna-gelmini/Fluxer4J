package flux.api;

public final class ApiEnvironment {

    public static final String DEFAULT_API_BASE_URL = "https://api.fluxer.app/v1";
    public static final String DEFAULT_GATEWAY_URL = "wss://gateway.fluxer.app";
    public static final String DEFAULT_GATEWAY_VERSION = "1";
    public static final String DEFAULT_MEDIA_CDN_URL = "https://fluxerusercontent.com";
    public static final String DEFAULT_APP_BASE_URL = "https://web.fluxer.app";

    private static volatile String apiBaseUrl = DEFAULT_API_BASE_URL;
    private static volatile String gatewayUrl = DEFAULT_GATEWAY_URL;
    private static volatile String gatewayVersion = DEFAULT_GATEWAY_VERSION;
    private static volatile String mediaCdnUrl = DEFAULT_MEDIA_CDN_URL;
    private static volatile String appBaseUrl = DEFAULT_APP_BASE_URL;

    private ApiEnvironment() {
    }

    public static void configure(String apiBase, String gateway, String mediaCdn, String appBase) {
        if (apiBase != null && !apiBase.isBlank()) {
            apiBaseUrl = normalizeApiBaseUrl(apiBase.trim());
        }
        if (gateway != null && !gateway.isBlank()) {
            gatewayUrl = stripTrailingSlash(gateway.trim());
        }
        if (mediaCdn != null && !mediaCdn.isBlank()) {
            mediaCdnUrl = stripTrailingSlash(mediaCdn.trim());
        }
        if (appBase != null && !appBase.isBlank()) {
            appBaseUrl = stripTrailingSlash(appBase.trim());
        }
    }

    public static String getApiBaseUrl() {
        return apiBaseUrl;
    }

    public static String getGatewayUrl() {
        return gatewayUrl;
    }

    public static String getGatewayVersion() {
        return gatewayVersion;
    }

    public static String getMediaCdnUrl() {
        return mediaCdnUrl;
    }

    public static String getAppBaseUrl() {
        return appBaseUrl;
    }

    public static String avatarUrl(String userId, String avatarHash) {
        String format = avatarHash.startsWith("a_") ? "gif" : "webp";
        return String.format("%s/avatars/%s/%s.%s?size=128", mediaCdnUrl, userId, avatarHash, format);
    }

    public static String defaultAvatarUrl(int index) {
        return String.format("%s/embed/avatars/%d.png", mediaCdnUrl, index);
    }

    public static String guildIconUrl(String guildId, String iconHash) {
        String format = iconHash.startsWith("a_") ? "gif" : "webp";
        return String.format("%s/icons/%s/%s.%s?size=128", mediaCdnUrl, guildId, iconHash, format);
    }

    public static String channelMessageUrl(String guildId, String channelId, String messageId) {
        return String.format("%s/channels/%s/%s/%s", appBaseUrl, guildId, channelId, messageId);
    }

    static String normalizeApiBaseUrl(String url) {
        String normalized = stripTrailingSlash(url);
        if (!normalized.endsWith("/v1")) {
            normalized = normalized + "/v1";
        }
        return normalized;
    }

    private static String stripTrailingSlash(String url) {
        return url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
    }
}
