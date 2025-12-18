package RadVeda.NotificationManagement.Notifications;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * WHAT IT IS:
 * This is a concrete Entity class for simple, one-way notifications.
 * 
 * WHY WE NEED IT:
 * Sometimes we just need to send a generic alert (e.g., "Your report is ready",
 * "Happy Birthday")
 * that doesn't need to link to a chat or a consent request.
 * 
 * WHY WE NEED IT ALL OF THAT:
 * 1. extends Notification: Inherits message, recipient, etc.
 * 2. @Entity: Creates a table.
 * 3. No extra fields: It doesn't need extra IDs or Types, so it just adds an ID
 * for itself
 * and relies on the inherited fields to do the job.
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class OneWayNotification extends Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
