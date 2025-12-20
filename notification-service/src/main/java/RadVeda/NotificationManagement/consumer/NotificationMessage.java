package RadVeda.NotificationManagement.consumer;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationMessage {
    private String type; // "CHAT", "CONSENT", "ONE_WAY"
    private String body; // The actual content
    private Long recipientId; // Who gets it
}
