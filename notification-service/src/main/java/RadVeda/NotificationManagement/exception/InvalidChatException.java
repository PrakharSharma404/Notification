package RadVeda.NotificationManagement.exception;

/**
 * WHAT IS THE POINT OF THIS FILE:
 * Custom Exception for invalid chat references.
 * WHY WE NEED IT:
 * Mapped to HTTP 400 (Bad Request) in GlobalExceptionHandler.
 */
public class InvalidChatException extends RuntimeException {
    public InvalidChatException(String message) {
        super(message);
    }
}
