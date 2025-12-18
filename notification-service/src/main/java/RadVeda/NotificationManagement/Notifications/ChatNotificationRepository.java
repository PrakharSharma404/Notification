package RadVeda.NotificationManagement.Notifications;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * WHAT IT IS:
 * Repository specifically for ChatNotification entities.
 * 
 * WHY WE NEED IT:
 * To interact with the 'chat_notification' table.
 * 
 * OVERALL AFFECT TO CODE AND LOGIC:
 * This interface works "cleanly" by extending NotificationBaseRepository.
 * It inherits all the common finder and deleter methods automatically.
 * 
 * WHY IT IS EMPTY:
 * All the heavy lifting is done by the parent `NotificationBaseRepository`.
 * This just types it to `ChatNotification`. Silence is golden.
 */
public interface ChatNotificationRepository extends NotificationBaseRepository<ChatNotification> {
    // Silence is golden.
}
