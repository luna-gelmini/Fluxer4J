package flux.api.entities.message.component;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ComponentType {
    ACTION_ROW(1),
    BUTTON(2),
    STRING_SELECT(3),
    TEXT_INPUT(4),
    USER_SELECT(5),
    ROLE_SELECT(6),
    MENTIONABLE_SELECT(7),
    CHANNEL_SELECT(8);

    private final int value;

    ComponentType(int value) {
        this.value = value;
    }

    @JsonValue
    public int getValue() {
        return value;
    }
}
