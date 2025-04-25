package utils;

/**
 * Exception thrown when JSON schema validation fails.
 */
public class JsonValidationException extends RuntimeException {
    
    /**
     * Constructs a new JsonValidationException with the specified detail message.
     *
     * @param message the detail message
     */
    public JsonValidationException(String message) {
        super(message);
    }

    /**
     * Constructs a new JsonValidationException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause
     */
    public JsonValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}