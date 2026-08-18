package flux.api.entities;

public enum TargetType {
    ROLE(0),
    MEMBER(1);
    private final int value;

    TargetType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
