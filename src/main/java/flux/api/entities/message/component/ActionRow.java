package flux.api.entities.message.component;

import java.util.List;

public interface ActionRow extends Component {
    List<Component> getComponents();

    @Override
    default ComponentType getType() {
        return ComponentType.ACTION_ROW;
    }
}
