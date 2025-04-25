package utils;

/**
 * Exception thrown when XML schema validation fails.
 */
public class XmlValidationException extends RuntimeException {
    
    /**
     * Constructs a new XmlValidationException with the specified detail message.
     *
     * @param message the detail message
     */
    public XmlValidationException(String message) {
        super(message);
    }

    /**
     * Constructs a new XmlValidationException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause
     */
    public XmlValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}