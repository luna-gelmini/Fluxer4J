package flux.api.payload.interaction;

public class CommandOptionChoice {
    private final String name;
    private final Object value;

    public CommandOptionChoice(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public CommandOptionChoice(String name, long value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }
}
