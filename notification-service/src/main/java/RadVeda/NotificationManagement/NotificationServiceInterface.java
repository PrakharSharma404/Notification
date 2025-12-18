package RadVeda.NotificationManagement;

import RadVeda.NotificationManagement.Notifications.*;
import java.util.List;

/**
 * WHAT IT IS:
 * This interface defines the contract for the Notification Service. It lists
 * all the
 * public operations that our application supports regarding notifications.
 * 
 * WHY WE NEED IT:
 * Interfaces decouple the "what" from the "how".
 * 1. "What": We need to find, send, and delete notifications.
 * 2. "How": The implementation class (NotificationService) will handle the
 * logic (calling repositories,
 * checking security, etc.).
 * By coding to an interface, we make our code cleaner and easier to test.
 * 
 * WHY WE NEED IT ALL OF THAT:
 * The methods are grouped by functionality:
 * 1. Retrieval (findAll...): For getting lists of notifications for a user.
 * 2. Single Item Access (find...ById): For clicking into a specific
 * notification. Note the 'User currentUser'
 * argument; this implies we will check if the user requesting the notification
 * is actually allowed to see it.
 * 3. Sending (send...): Operations to create new notifications. They return a
 * String (likely a success message).
 * 4. Deletion (delete...): Methods to remove notifications (single or bulk).
 * 5. Validation/Helper: Methods like 'authenticate' and 'isRecipientValid'
 * suggest this service also handles
 * some cross-cutting security concerns.
 */
public interface NotificationServiceInterface {
        // Methods to retrieve notifications
        List<ChatNotification> findAllChatNotificationsByRecipient(String recipientType, Long recipientId);

        List<ConsentRequestNotification> findAllConsentRequestNotificationsByRecipient(String recipientType,
                        Long recipientId);

        List<OneWayNotification> findAllOneWayNotificationsByRecipient(String recipientType, Long recipientId);

        // Methods to find a single notification (with security check)
        ChatNotification findChatNotificationById(Long Id, User currentUser);

        ConsentRequestNotification findConsentRequestNotificationById(Long Id, User currentUser);

        OneWayNotification findOneWayNotificationById(Long Id, User currentUser);

        // Methods to send notifications
        void sendChatNotificationToRecipient(String message, String recipientType, Long recipientId, String chatType,
                        Long chatId);

        void sendConsentRequestNotificationToRecipient(String message, String recipientType, Long recipientId,
                        Long consentRequestId);

        void sendOneWayNotificationToRecipient(String message, String recipientType, Long recipientId);

        // Methods to delete specific notifications
        String deleteChatNotificationOfRecipient(Long Id, User currentUser);

        String deleteConsentRequestNotificationOfRecipient(Long Id, User currentUser);

        String deleteOneWayNotificationOfRecipient(Long Id, User currentUser);

        // Methods to delete all notifications for a user
        String deleteAllChatNotificationsOfRecipient(String recipientType, Long recipientId);

        String deleteAllConsentRequestNotificationsOfRecipient(String recipientType, Long recipientId);

        String deleteAllOneWayNotificationsOfRecipient(String recipientType, Long recipientId);

        // Helper methods for validation
        User authenticate(String authorizationHeader);

        boolean isRecipientValid(String recipientType, Long recipientId, User currentUser);

        boolean isChatValid(String chatType, Long chatId, User currentUser);

        boolean isConsentRequestValid(Long consentRequestId, User currentUser);
}
