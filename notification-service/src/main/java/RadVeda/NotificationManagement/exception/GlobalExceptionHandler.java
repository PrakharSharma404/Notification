package RadVeda.NotificationManagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
/**
 * WHAT IS THE POINT OF THIS FILE:
 * The "Safety Net" for our application. It listens for Exceptions thrown
 * anywhere in the Controller.
 * 
 * WHY WE NEED IT:
 * Instead of writing try-catch blocks in every single controller method, we let
 * the exception propagate up.
 * This class catches it, determines the correct HTTP Status (e.g., 404 for
 * NotFound), creates a standardized
 * `ErrorResponse` JSON, and sends it to the user.
 */
public class GlobalExceptionHandler {

    @ExceptionHandler(UnauthorisedUserException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorisedUserException(UnauthorisedUserException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.FORBIDDEN.value(),
                HttpStatus.FORBIDDEN.getReasonPhrase(),
                ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(RecipientNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleRecipientNotFoundException(RecipientNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidChatException.class)
    public ResponseEntity<ErrorResponse> handleInvalidChatException(InvalidChatException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidConsentRequestException.class)
    public ResponseEntity<ErrorResponse> handleInvalidConsentRequestException(InvalidConsentRequestException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotificationNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotificationNotFoundException(NotificationNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}
