package RadVeda.NotificationManagement.Notifications;

import RadVeda.NotificationManagement.StorageEncryption.Converters.EncryptedLongConverter;
import RadVeda.NotificationManagement.StorageEncryption.Converters.EncryptedStringConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * WHAT IT IS:
 * This is a concrete Entity class explicitly for Chat Notifications.
 * 
 * WHY WE NEED IT:
 * Different notifications have different data requirements. A Chat Notification
 * specifically
 * needs to know *which* chat it belongs to and what *type* of chat it is.
 * 
 * WHY WE NEED IT ALL OF THAT:
 * 1. extends Notification: It inherits the common fields (message, recipientId,
 * etc.) from the base class.
 * 2. @Entity: Marks this class as a JPA Entity, meaning it will have its own
 * table in the database
 * (likely 'chat_notification').
 * 3. @Id, @GeneratedValue: Defines the Primary Key for this specific table and
 * sets it to auto-increment.
 * 4. @Convert: Again, we use our custom converters to encrypt the sensitive
 * chat-specific data:
 * - chatType: e.g., "CONSULTATION", "SUPPORT" (EncryptedStringConverter)
 * - chatId: The ID of the chat room/session (EncryptedLongConverter)
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ChatNotification extends Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = EncryptedStringConverter.class)
    private String chatType;

    @Convert(converter = EncryptedLongConverter.class)
    private Long chatId;
}
