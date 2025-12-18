package RadVeda.NotificationManagement.exception;

/**
 * WHAT IT IS:
 * A custom exception thrown when we try to send a notification to a user that
 * doesn't exist.
 * 
 * WHAT IS THE POINT OF THIS FILE:
 * Custom Exception for unknown recipients.
 * WHY WE NEED IT:
 * Mapped to HTTP 404 (Not Found) in GlobalExceptionHandler.
 */
public class RecipientNotFoundException extends RuntimeException {
    public RecipientNotFoundException(String message) {
        super(message);
    }
}
