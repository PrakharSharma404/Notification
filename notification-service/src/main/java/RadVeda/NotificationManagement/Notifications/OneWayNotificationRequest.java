package RadVeda.NotificationManagement.Notifications;

/**
 * WHAT IT IS:
 * A Data Transfer Object (DTO) for creating a new One-Way Notification.
 * 
 * WHY WE NEED IT:
 * To capture the simple message and recipient data for basic alerts.
 * 
 * WHY WE NEED IT ALL OF THAT:
 * Java Record for immutable, concise data transfer.
 */
public record OneWayNotificationRequest(
        String message,
        String recipientType,
        Long recipientId) {
}
