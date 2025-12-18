package RadVeda.NotificationManagement.exception;

import java.time.LocalDateTime;

/**
 * WHAT IS THE POINT OF THIS FILE:
 * A standard Data Structure (Record) for sending error details to the client.
 * 
 * WHY WE NEED IT:
 * When an API fails, we don't just want to return a 500 status. We want to send
 * JSON
 * explaining *what* happened (message) and *when* (timestamp).
 * This ensures the frontend gets a consistent error format for every failure.
 */
public record ErrorResponse(
                LocalDateTime timestamp,
                int status,
                String error,
                String message) {
}
