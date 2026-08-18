package flux.api.entities.message.component;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ButtonStyle {
    PRIMARY(1),
    SECONDARY(2),
    SUCCESS(3),
    DANGER(4),
    LINK(5);

    private final int value;

    ButtonStyle(int value) {
        this.value = value;
    }

    @JsonValue
    public int getValue() {
        return value;
    }
}
