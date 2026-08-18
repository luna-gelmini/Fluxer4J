package flux.model.message.component;

import com.fasterxml.jackson.annotation.JsonProperty;
import flux.api.entities.message.component.ActionRow;
import flux.api.entities.message.component.Component;
import flux.api.entities.message.component.ComponentType;

import java.util.ArrayList;
import java.util.List;

public class ActionRowImpl implements ActionRow {

    private final int type = ComponentType.ACTION_ROW.getValue();

    @JsonProperty("components")
    private List<Component> components = new ArrayList<>();

    public ActionRowImpl() {}

    public ActionRowImpl(List<Component> components) {
        this.components = components;
    }

    @Override
    public List<Component> getComponents() {
        return components;
    }

    public void setComponents(List<Component> components) {
        this.components = components;
    }

    @Override
    @JsonProperty("type")
    public ComponentType getType() {
        return ComponentType.ACTION_ROW;
    }
}
