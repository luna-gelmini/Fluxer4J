package flux.api.event;

@FunctionalInterface
public interface EventListener {
    void onEvent(Event event);
}
