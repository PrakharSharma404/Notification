package RadVeda.NotificationManagement.Notifications;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * WHAT IT IS:
 * A Generic Base Repository interface that acts as a template for all
 * Notification types.
 * 
 * WHY WE NEED IT:
 * To stop rewriting the exact same queries (findByRecipient, deleteByRecipient)
 * for every singe notification type.
 * This adheres to the DRY (Don't Repeat Yourself) principle.
 * 
 * OVERALL AFFECT TO CODE AND LOGIC:
 * 1. Centralization: Common DB logic lives here. If we fix a bug in the query,
 * it's fixed for ALL notifications.
 * 2. Polymorphism: Allows the Service layer to treat all notifications
 * identically, enabling generic helper methods.
 * 3. Simplicity: Child repositories become empty interfaces, just inheriting
 * this power.
 */
@NoRepositoryBean // Tells Spring: "Don't try to build this, it's a template."
public interface NotificationBaseRepository<T extends Notification> extends JpaRepository<T, Long> {

    // One method to rule them all.
    List<T> findByRecipientTypeAndRecipientId(String recipientType, Long recipientId);

    // One transactional delete to keep the DB clean.
    @Transactional
    void deleteByRecipientTypeAndRecipientId(@Param("recipientType") String recipientType,
            @Param("recipientId") Long recipientId);
}
