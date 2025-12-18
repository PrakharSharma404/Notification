package RadVeda.NotificationManagement.exception;

/**
 * WHAT IT IS:
 * A custom exception thrown when a specific notification cannot be found.
 * 
 * WHY WE NEED IT:
 * When a user tries to fetch or delete a notification by ID, and that ID
 * doesn't exist,
 * we need a clear way to signal this error so the API can return a 404 Not
 * Found status.
 * Mapped to HTTP 404 (Not Found) in GlobalExceptionHandler.
 */
public class NotificationNotFoundException extends RuntimeException {
    public NotificationNotFoundException(String message) {
        super(message);
    }
}
