package RadVeda.NotificationManagement;

import RadVeda.NotificationManagement.Notifications.*;
import RadVeda.NotificationManagement.config.CurrentUser;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * WHAT IT IS:
 * The REST Controller for the Notification Service.
 * 
 * WHY WE NEED IT:
 * This is the "Doorway" to our service. It exposes the functionality (send,
 * get, delete notifications)
 * via HTTP endpoints (GET, POST, DELETE) so that the Frontned (React/Next.js)
 * or other Microservices
 * can interact with us.
 * 
 * WHY WE NEED IT ALL OF THAT:
 * 1. @RestController: Tells Spring that this class handles web requests and
 * returns JSON responses directly.
 * 2. @RequestMapping("/notifications"): All endpoints in this class will start
 * with /notifications.
 * 3. @RequestHeader("Authorization"): We grab the JWT token from the header to
 * authenticate the user for every request.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    // --- GET Endpoints ---

    @GetMapping("/getAllChatNotifications")
    public List<ChatNotification> getAllChatNotifications(
            @CurrentUser User currentUser) {
        return notificationService.findAllChatNotificationsByRecipient(currentUser.getType(), currentUser.getId());
    }

    @GetMapping("/getAllConsentRequestNotifications")
    public List<ConsentRequestNotification> getAllConsentRequestNotifications(
            @CurrentUser User currentUser) {
        return notificationService.findAllConsentRequestNotificationsByRecipient(currentUser.getType(),
                currentUser.getId());
    }

    @GetMapping("/getAllOneWayNotifications")
    public List<OneWayNotification> getAllOneWayNotifications(
            @CurrentUser User currentUser) {
        return notificationService.findAllOneWayNotificationsByRecipient(currentUser.getType(), currentUser.getId());
    }

    // --- POST Endpoints (Sending Notifications) ---

    // --- POST Endpoints (Removed: Now handled via RabbitMQ Events) ---

    // --- DELETE Endpoints ---

    @DeleteMapping("/deleteChatNotification/{id}")
    public String deleteChatNotification(
            @PathVariable Long id,
            @CurrentUser User currentUser) {
        return notificationService.deleteChatNotificationOfRecipient(id, currentUser);
    }

    @DeleteMapping("/deleteAllChatNotifications")
    public String deleteAllChatNotifications(
            @CurrentUser User currentUser) {
        return notificationService.deleteAllChatNotificationsOfRecipient(currentUser.getType(), currentUser.getId());
    }

    // (Note: You can add the remaining delete endpoints for Consent and OneWay
    // notifications similarly if needed)
}
