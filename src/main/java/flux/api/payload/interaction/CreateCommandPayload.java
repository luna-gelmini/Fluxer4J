package flux.api.payload.interaction;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateCommandPayload {

    @JsonProperty("name")
    private final String name;

    @JsonProperty("description")
    private final String description;

    @JsonProperty("options")
    private final List<CommandOption> options;

    public CreateCommandPayload(String name, String description, List<CommandOption> options) {
        this.name = name;
        this.description = description;
        this.options = options;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class CommandOption {
        @JsonProperty("type")
        public int type;
        @JsonProperty("name")
        public String name;
        @JsonProperty("description")
        public String description;
        @JsonProperty("required")
        public Boolean required;
        @JsonProperty("choices")
        public List<CommandChoice> choices;
        @JsonProperty("options")
        public List<CommandOption> options;

        public CommandOption(int type, String name, String description, boolean required) {
            this.type = type;
            this.name = name;
            this.description = description;
            this.required = required;
        }

        public CommandOption(int type, String name, String description) {
            this.type = type;
            this.name = name;
            this.description = description;
            this.required = null;
        }

        public CommandOption withOptions(List<CommandOption> options) {
            this.options = options;
            return this;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class CommandChoice {
        @JsonProperty("name")
        public String name;
        @JsonProperty("value")
        public Object value;

        public CommandChoice(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public CommandChoice(String name, int value) {
            this.name = name;
            this.value = value;
        }
    }
}
