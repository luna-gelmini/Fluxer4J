package flux.api.payload.interaction;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ModalPayload {

    @JsonProperty("custom_id")
    private final String customId;

    @JsonProperty("title")
    private final String title;

    @JsonProperty("components")
    private final List<ActionRow> components;

    public ModalPayload(String customId, String title, List<ActionRow> components) {
        this.customId = customId;
        this.title = title;
        this.components = components;
    }

    public static ModalPayload create(String customId, String title) {
        return new ModalPayload(customId, title, new java.util.ArrayList<>());
    }

    public ModalPayload addTextInput(String id, String label, int style, boolean required,
            String placeholder, Integer minLength, Integer maxLength, String value) {
        TextInput input = new TextInput(id, label, style, required, placeholder, minLength, maxLength, value);
        this.components.add(new ActionRow(List.of(input)));
        return this;
    }

    public ModalPayload addShortTextInput(String id, String label, boolean required, String placeholder) {
        return addTextInput(id, label, 1, required, placeholder, null, null, null);
    }

    public ModalPayload addParagraphTextInput(String id, String label, boolean required, String placeholder) {
        return addTextInput(id, label, 2, required, placeholder, null, null, null);
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ActionRow {
        @JsonProperty("type")
        private final int type = 1;

        @JsonProperty("components")
        private final List<TextInput> components;

        public ActionRow(List<TextInput> components) {
            this.components = components;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class TextInput {
        @JsonProperty("type")
        private final int type = 4;

        @JsonProperty("custom_id")
        private final String customId;

        @JsonProperty("label")
        private final String label;

        @JsonProperty("style")
        private final int style;

        @JsonProperty("required")
        private final boolean required;

        @JsonProperty("placeholder")
        private final String placeholder;

        @JsonProperty("min_length")
        private final Integer minLength;

        @JsonProperty("max_length")
        private final Integer maxLength;

        @JsonProperty("value")
        private final String value;

        public TextInput(String customId, String label, int style, boolean required,
                String placeholder, Integer minLength, Integer maxLength, String value) {
            this.customId = customId;
            this.label = label;
            this.style = style;
            this.required = required;
            this.placeholder = placeholder;
            this.minLength = minLength;
            this.maxLength = maxLength;
            this.value = value;
        }
    }
}
