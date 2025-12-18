package RadVeda.NotificationManagement.Notifications;

/**
 * WHAT IT IS:
 * A Data Transfer Object (DTO) for creating a new Chat Notification.
 * 
 * WHY WE NEED IT:
 * When a client sends a POST request to create a notification, we need a simple
 * object
 * to capture that JSON data. Java Records are perfect for this as they are
 * immutable data carriers.
 * 
 * WHY WE NEED IT ALL OF THAT:
 * 'record' automatically gives us a constructor, getters, equals(), hashCode(),
 * and toString().
 */
public record ChatNotificationRequest(
        String message,
        String recipientType,
        Long recipientId,
        String chatType,
        Long chatId) {
}
