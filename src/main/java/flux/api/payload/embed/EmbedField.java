package flux.api.payload.embed;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmbedField {
    @JsonProperty("name")
    public String name;

    @JsonProperty("value")
    public String value;

    @JsonProperty("inline")
    public Boolean inline;

    public EmbedField(String name, String value, Boolean inline) {
        this.name = name;
        this.value = value;
        this.inline = inline;
    }

    public EmbedField() {
    }
}
