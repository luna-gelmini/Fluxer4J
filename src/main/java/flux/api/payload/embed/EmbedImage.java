package flux.api.payload.embed;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmbedImage {
    @JsonProperty("url")
    public String url;

    public EmbedImage(String url) {
        this.url = url;
    }

    public EmbedImage() {
    }
}
