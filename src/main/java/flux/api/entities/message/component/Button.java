package flux.api.entities.message.component;

public interface Button extends Component {
    ButtonStyle getStyle();
    String getLabel();
    String getCustomId();
    String getUrl();
    boolean isDisabled();

    @Override
    default ComponentType getType() {
        return ComponentType.BUTTON;
    }
}
