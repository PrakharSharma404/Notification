package RadVeda.NotificationManagement.Notifications;

/**
 * WHAT IT IS:
 * A Data Transfer Object (DTO) for creating a new Consent Request Notification.
 * 
 * WHY WE NEED IT:
 * To capture the specific data needed for consent requests (like
 * 'consentRequestId') from the API request body.
 * 
 * WHY WE NEED IT ALL OF THAT:
 * Java Record syntax reduces boilerplate code for DTOs.
 */
public record ConsentRequestNotificationRequest(
        String message,
        String recipientType,
        Long recipientId,
        Long consentRequestId) {
}
