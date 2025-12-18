package RadVeda.NotificationManagement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * WHAT IT IS:
 * This class represents a simplified User model within our Notification
 * Service.
 * 
 * WHY WE NEED IT:
 * The notification service needs to know *who* it is interacting with (e.g.,
 * who is sending a request,
 * who is the recipient). Since authentication is likely handled elsewhere (like
 * an API Gateway or Auth Service),
 * we need a simple object to hold the user's identity details when we validate
 * or process requests.
 * 
 * WHY WE NEED IT ALL OF THAT:
 * 1. Fields (type, id): These are the two keys to uniquely identifying any user
 * in our system.
 * - type: "DOCTOR", "PATIENT", "ADMIN", etc.
 * - id: The primary key of that user in their respective database table.
 * 2. Lombok Annotations: eliminate the need to write getters, setters, and
 * constructors manually.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String type; // e.g., "DOCTOR", "PATIENT"
    private Long id;
    private String token;
}
