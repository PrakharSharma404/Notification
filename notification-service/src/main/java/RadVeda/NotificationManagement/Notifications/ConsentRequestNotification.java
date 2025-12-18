package RadVeda.NotificationManagement.Notifications;

import RadVeda.NotificationManagement.StorageEncryption.Converters.EncryptedLongConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * WHAT IT IS:
 * This is a concrete Entity class for Consent Request Notifications.
 * 
 * WHY WE NEED IT:
 * These notifications happen when a doctor (or other entity) requests consent
 * to view a patient's
 * records. Note that this needs different data than a Chat Notification.
 * 
 * WHY WE NEED IT ALL OF THAT:
 * 1. extends Notification: Inherits common behavior.
 * 2. @Entity: Created a dedicated table for this notification type.
 * 3. @Convert: Encrypts the specific 'consentRequestId'. This ID likely links
 * to a record in
 * another service or table. We encrypt it to prevent leaking business
 * intelligence about
 * request volumes or patterns if the DB is inspected.
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ConsentRequestNotification extends Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = EncryptedLongConverter.class)
    private Long consentRequestId;
}
