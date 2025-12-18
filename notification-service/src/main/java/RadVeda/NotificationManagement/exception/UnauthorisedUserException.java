package RadVeda.NotificationManagement.exception;

/**
 * WHAT IT IS:
 * A custom exception thrown when a user tries to access a notification they
 * don't own.
 * 
 * WHAT IS THE POINT OF THIS FILE:
 * Security Exception for unauthorized access attempts.
 * WHY WE NEED IT:
 * Mapped to HTTP 403 (Forbidden) in GlobalExceptionHandler.
 */
public class UnauthorisedUserException extends RuntimeException {
    public UnauthorisedUserException(String message) {
        super(message);
    }
}
