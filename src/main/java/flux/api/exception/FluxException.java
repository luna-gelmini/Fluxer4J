package flux.api.exception;

public class FluxException extends RuntimeException {
    public FluxException(String message) {
        super(message);
    }

    public FluxException(String message, Throwable cause) {
        super(message, cause);
    }
}
