package flux.api.payload.embed;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmbedThumbnail {
    @JsonProperty("url")
    public String url;

    public EmbedThumbnail(String url) {
        this.url = url;
    }

    public EmbedThumbnail() {
    }
}
