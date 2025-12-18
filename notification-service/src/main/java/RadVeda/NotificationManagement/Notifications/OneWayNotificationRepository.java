package RadVeda.NotificationManagement.Notifications;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * WHAT IT IS:
 * Repository specifically for OneWayNotification entities.
 * 
 * WHY WE NEED IT:
 * To interact with the 'one_way_notification' table.
 * 
 * OVERALL AFFECT TO CODE AND LOGIC:
 * 1. Extends `NotificationBaseRepository` to inherit common queries.
 * 2. Keeps the codebase DRY (Don't Repeat Yourself).
 */
public interface OneWayNotificationRepository extends NotificationBaseRepository<OneWayNotification> {
    // Silence is golden.
}
