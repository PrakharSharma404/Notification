package RadVeda.NotificationManagement.Notifications;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * WHAT IT IS:
 * This interface is a Spring Data JPA Repository specifically for
 * ConsentRequestNotification entities.
 * Repository specifically for ConsentRequestNotification entities.
 * 
 * WHY WE NEED IT:
 * To interact with the 'consent_request_notification' table.
 * 
 * OVERALL AFFECT TO CODE AND LOGIC:
 * 1. Extends `NotificationBaseRepository` to inherit common queries.
 * 2. Drastically reduces boilerplate code.
 * 3. Ensures consistent behavior with other notification types.
 */
public interface ConsentRequestNotificationRepository extends NotificationBaseRepository<ConsentRequestNotification> {
    // Silence is golden.
}
