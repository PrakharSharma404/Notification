package RadVeda.NotificationManagement.exception;

/**
 * WHAT IT IS:
 * A custom exception thrown when a Consent Request Notification references an
 * invalid Consent Request ID.
 * WHAT IS THE POINT OF THIS FILE:
 * Custom Exception for invalid consent requests.
 * WHY WE NEED IT:
 * Mapped to HTTP 400 (Bad Request) in GlobalExceptionHandler.
 */
public class InvalidConsentRequestException extends RuntimeException {
    public InvalidConsentRequestException(String message) {
        super(message);
    }
}
