package flux.api.payload.embed;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmbedAuthor {
    @JsonProperty("name")
    public String name;

    @JsonProperty("url")
    public String url;

    @JsonProperty("icon_url")
    public String iconUrl;

    public EmbedAuthor(String name, String url, String iconUrl) {
        this.name = name;
        this.url = url;
        this.iconUrl = iconUrl;
    }

    public EmbedAuthor() {
    }
}
