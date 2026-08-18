package flux.model.message.component;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import flux.api.entities.message.component.ButtonStyle;
import flux.api.entities.message.component.Button;
import flux.api.entities.message.component.ComponentType;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ButtonImpl implements Button {

    private final int type = ComponentType.BUTTON.getValue();

    @JsonProperty("style")
    private ButtonStyle style;

    @JsonProperty("label")
    private String label;

    @JsonProperty("custom_id")
    private String customId;

    @JsonProperty("url")
    private String url;

    @JsonProperty("disabled")
    private boolean disabled;

    public ButtonImpl() {}

    public ButtonImpl(ButtonStyle style, String label, String customId) {
        this.style = style;
        this.label = label;
        this.customId = customId;
    }

    @Override
    public ButtonStyle getStyle() {
        return style;
    }

    public void setStyle(ButtonStyle style) {
        this.style = style;
    }

    @Override
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String getCustomId() {
        return customId;
    }

    public void setCustomId(String customId) {
        this.customId = customId;
    }

    @Override
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    @Override
    @JsonProperty("type")
    public ComponentType getType() {
        return ComponentType.BUTTON;
    }
}
