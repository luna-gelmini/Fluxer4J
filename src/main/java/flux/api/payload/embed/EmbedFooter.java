package flux.api.payload.embed;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmbedFooter {
    @JsonProperty("text")
    public String text;

    @JsonProperty("icon_url")
    public String iconUrl;

    public EmbedFooter(String text, String iconUrl) {
        this.text = text;
        this.iconUrl = iconUrl;
    }

    public EmbedFooter() {
    }
}
