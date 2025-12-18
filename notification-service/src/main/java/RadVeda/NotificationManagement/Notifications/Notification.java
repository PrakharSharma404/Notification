package RadVeda.NotificationManagement.Notifications;

import RadVeda.NotificationManagement.StorageEncryption.Converters.EncryptedLongConverter;
import RadVeda.NotificationManagement.StorageEncryption.Converters.EncryptedStringConverter;
import jakarta.persistence.Convert;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * WHAT IT IS:
 * This is an abstract base class for all Notification types (e.g.,
 * EmailNotification, SmsNotification).
 * 
 * WHY WE NEED IT:
 * It holds common fields and behaviors that all notifications share. Using
 * inheritance prevents
 * code duplication.
 * 
 * WHY WE NEED IT ALL OF THAT:
 * 1. @MappedSuperclass: This is a key JPA annotation. It means this class is
 * NOT an entity itself
 * (it has no separate database table), but its fields will be mapped to the
 * columns of the
 * concrete subclasses (like an 'email_notifications' table).
 * 2. @Getter, @Setter, @NoArgsConstructor, @AllArgsConstructor: Lombok
 * annotations to automatically
 * generate boilerplate code like getters, setters, and constructors.
 * 3. @Convert(converter = ...): These are crucial for security. They tell JPA
 * to use our
 * custom converters (EncryptedStringConverter, EncryptedLongConverter) to
 * encrypt these specific
 * fields before writing to the DB and decrypt them when reading. We don't have
 * to write any
 * encryption logic in our service layer; it happens automatically here.
 */
@Getter
@Setter
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
public abstract class Notification {

    // The core message content of the notification. Encrypted in DB.
    @Convert(converter = EncryptedStringConverter.class)
    private String message;

    // Type of recipient (e.g. "USER", "ADMIN"). Encrypted in DB.
    @Convert(converter = EncryptedStringConverter.class)
    private String recipientType;

    // The ID of the recipient. Encrypted in DB to protect identity.
    @Convert(converter = EncryptedLongConverter.class)
    private Long recipientId;
}
