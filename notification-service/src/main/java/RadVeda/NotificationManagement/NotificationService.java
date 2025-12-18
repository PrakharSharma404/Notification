package RadVeda.NotificationManagement;

import RadVeda.NotificationManagement.Notifications.*;
import RadVeda.NotificationManagement.exception.NotificationNotFoundException;
import RadVeda.NotificationManagement.exception.UnauthorisedUserException;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

/**
 * WHAT IT IS:
 * This is the implementation of the NotificationServiceInterface (the "Core
 * Business Logic").
 * 
 * WHY WE NEED IT:
 * This class connects all the moving parts:
 * 1. It talks to the Repositories to save/fetch data from the database.
 * 2. It implements the Validation Logic (checking if a user owns a notification
 * before showing it).
 * 3. It communicates with OTHER services (via RestTemplate) to verify tokens,
 * valid chat IDs, etc.
 * 
 * WHY WE NEED IT ALL OF THAT:
 * 1. @Service: Tells Spring "This is a Service bean", so it can be injected
 * into Controllers.
 * 2. @RequiredArgsConstructor: Lombok annotation that generates a constructor
 * for all 'final' fields.
 * This is how we do "Dependency Injection" for the repositories.
 * 
 * UPDATED LOGIC (GENERIC REFACTOR):
 * This class has been heavily refactored to use GENERIC methods.
 * Instead of writing the same "Find -> Check Null -> Validate Security" logic 3
 * times,
 * we now use `fetchAndValidate` and `deleteSecurely`.
 * This reduces line count, improves maintainability, and ensures consistent
 * security checks.
 */
@Service
@RequiredArgsConstructor
public class NotificationService implements NotificationServiceInterface {
    private final ChatNotificationRepository chatNotificationRepository;
    private final ConsentRequestNotificationRepository consentRequestNotificationRepository;
    private final OneWayNotificationRepository oneWayNotificationRepository;

    @Value("${external-services.user-management.url}")
    private String userManagementUrl;

    @Value("${external-services.collaboration.url}")
    private String collaborationUrl;

    @Value("${external-services.consent.url}")
    private String consentUrl;

    // ------------------------------------------------------------------------------------------------
    // GENERIC HELPERS
    // ------------------------------------------------------------------------------------------------

    /**
     * GENERIC HELPER: fetchAndValidate
     * 
     * WHAT IT DOES:
     * 1. Fetches a notification by ID using the provided repository.
     * 2. Throws NotificationNotFoundException if missing.
     * 3. **CRITICAL**: Validates that the `currentUser` actually OWNS this
     * notification.
     * 
     * WHY WE NEED IT:
     * This logic was previously repeated 3 times (once for each notification type).
     * Now it is written once.
     * 
     * @param id          The ID of the notification
     * @param currentUser The user trying to access it
     * @param repository  The specific repository (Chat, Consent, etc.) to use
     * @return The found and validated notification
     * @throws UnauthorisedUserException if the user doesn't own the notification.
     */
    private <T extends Notification> T fetchAndValidate(
            Long id,
            User currentUser,
            NotificationBaseRepository<T> repository) {

        T notification = repository.findById(id)
                .orElseThrow(() -> new NotificationNotFoundException("Couldn't find a notification with the given ID"));

        if (!Objects.equals(currentUser.getId(), notification.getRecipientId())
                || !Objects.equals(currentUser.getType(), notification.getRecipientType())) {
            throw new UnauthorisedUserException("Permission denied!");
        }

        return notification;
    }

    /**
     * GENERIC HELPER: deleteSecurely
     * 
     * WHAT IT DOES:
     * 1. Calls `fetchAndValidate` to ensure the notification exists and the user
     * owns it.
     * 2. Deletes the notification.
     * 
     * OVERALL AFFECT:
     * Centralizes the "Secure Delete" pattern.
     */
    private <T extends Notification> String deleteSecurely(
            Long id,
            User currentUser,
            NotificationBaseRepository<T> repository) {

        T notification = fetchAndValidate(id, currentUser, repository);
        repository.delete(notification);
        return "Notification deleted successfully!!";
    }

    // ------------------------------------------------------------------------------------------------
    // RETRIEVAL METHODS
    // ------------------------------------------------------------------------------------------------

    @Override
    public List<ChatNotification> findAllChatNotificationsByRecipient(String recipientType, Long recipientId) {
        // Simply fetches all chat notifications for a specific user type (e.g.
        // "DOCTOR") and ID.
        return chatNotificationRepository.findByRecipientTypeAndRecipientId(recipientType, recipientId);
    }

    @Override
    public List<ConsentRequestNotification> findAllConsentRequestNotificationsByRecipient(String recipientType,
            Long recipientId) {
        return consentRequestNotificationRepository.findByRecipientTypeAndRecipientId(recipientType, recipientId);
    }

    @Override
    public List<OneWayNotification> findAllOneWayNotificationsByRecipient(String recipientType, Long recipientId) {
        return oneWayNotificationRepository.findByRecipientTypeAndRecipientId(recipientType, recipientId);
    }

    /**
     * Finds a single Chat Notification.
     * WHY WE NEED SECURITY CHECK HERE:
     * We don't want User A to be able to read User B's notifications just by
     * guessing the ID.
     * So, we check if the 'currentUser' matches the 'recipient' of the
     * notification.
     */
    @Override
    public ChatNotification findChatNotificationById(Long id, User currentUser) {
        return fetchAndValidate(id, currentUser, chatNotificationRepository);
    }

    @Override
    public ConsentRequestNotification findConsentRequestNotificationById(Long id, User currentUser) {
        return fetchAndValidate(id, currentUser, consentRequestNotificationRepository);
    }

    @Override
    public OneWayNotification findOneWayNotificationById(Long id, User currentUser) {
        return fetchAndValidate(id, currentUser, oneWayNotificationRepository);
    }

    // ------------------------------------------------------------------------------------------------
    // SENDING METHODS
    // ------------------------------------------------------------------------------------------------

    @Override
    public void sendChatNotificationToRecipient(String message, String recipientType, Long recipientId,
            String chatType, Long chatId) {
        ChatNotification chatNotif = new ChatNotification();
        // Setting fields. Note: Encryption happens automatically by the Converters in
        // the Entity class when .save() is called.
        chatNotif.setMessage(message);
        chatNotif.setRecipientType(recipientType);
        chatNotif.setRecipientId(recipientId);
        chatNotif.setChatId(chatId);
        chatNotif.setChatType(chatType);

        chatNotificationRepository.save(chatNotif);

    }

    @Override
    public void sendConsentRequestNotificationToRecipient(String message, String recipientType, Long recipientId,
            Long consentRequestId) {
        ConsentRequestNotification consReqNotif = new ConsentRequestNotification();

        consReqNotif.setMessage(message);
        consReqNotif.setRecipientType(recipientType);
        consReqNotif.setRecipientId(recipientId);
        consReqNotif.setConsentRequestId(consentRequestId);

        consentRequestNotificationRepository.save(consReqNotif);

    }

    @Override
    public void sendOneWayNotificationToRecipient(String message, String recipientType, Long recipientId) {
        OneWayNotification oneWayNotif = new OneWayNotification();

        oneWayNotif.setMessage(message);
        oneWayNotif.setRecipientType(recipientType);
        oneWayNotif.setRecipientId(recipientId);

        oneWayNotificationRepository.save(oneWayNotif);

    }

    // ------------------------------------------------------------------------------------------------
    // DELETE METHODS (SINGLE)
    // ------------------------------------------------------------------------------------------------

    @Override
    public String deleteChatNotificationOfRecipient(Long id, User currentUser) {
        return deleteSecurely(id, currentUser, chatNotificationRepository);
    }

    @Override
    public String deleteConsentRequestNotificationOfRecipient(Long id, User currentUser) {
        return deleteSecurely(id, currentUser, consentRequestNotificationRepository);
    }

    @Override
    public String deleteOneWayNotificationOfRecipient(Long id, User currentUser) {
        return deleteSecurely(id, currentUser, oneWayNotificationRepository);
    }

    // ------------------------------------------------------------------------------------------------
    // DELETE METHODS (ALL)
    // ------------------------------------------------------------------------------------------------

    @Override
    public String deleteAllChatNotificationsOfRecipient(String recipientType, Long recipientId) {
        // Uses the custom transactional query in the repository to wipe all
        // notifications for this user.
        chatNotificationRepository.deleteByRecipientTypeAndRecipientId(recipientType, recipientId);
        return "Notifications deleted successfully!!";
    }

    @Override
    public String deleteAllConsentRequestNotificationsOfRecipient(String recipientType, Long recipientId) {
        consentRequestNotificationRepository.deleteByRecipientTypeAndRecipientId(recipientType, recipientId);
        return "Notifications deleted successfully!!";
    }

    @Override
    public String deleteAllOneWayNotificationsOfRecipient(String recipientType, Long recipientId) {
        oneWayNotificationRepository.deleteByRecipientTypeAndRecipientId(recipientType, recipientId);
        return "Notifications deleted successfully!!";
    }

    // ------------------------------------------------------------------------------------------------
    // HELPER METHODS (AUTHENTICATION & VALIDATION)
    // ------------------------------------------------------------------------------------------------

    /**
     * Authenticates the user by calling an external User Management Service.
     * 
     * WHY WE NEED IT:
     * This service is stateless. It receives a JWT token ('authorizationHeader').
     * It needs to know "Who sent this token?". It asks the other services "Hey, who
     * owns this token?"
     * by trying to fetch the profile from each service (Admin, Doctor, Patient,
     * etc.) until one works.
     */
    @Override
    public User authenticate(String authorizationHeader) {
        String jwtToken = "";

        if (authorizationHeader != null && !authorizationHeader.isEmpty()) {
            jwtToken = authorizationHeader.replace("Bearer ", "");
        } else {
            return null;
        }

        try {
            // Logic:
            // 1. Split the token into its 3 parts (Header, Body, Signature)
            // 2. Decode the Body (Part 2) using standard Base64.getUrlDecoder()
            // 3. Parse the JSON using ObjectMapper

            String[] chunks = jwtToken.split("\\.");
            if (chunks.length < 3) {
                return null;
            }

            String payload = new String(Base64.getUrlDecoder().decode(chunks[1]));

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode payloadNode = objectMapper.readTree(payload);

            if (!payloadNode.has("role")) {
                return null;
            }

            String role = payloadNode.get("role").asText();

            // 3. Determine the Validation URL based on the Role
            String validationUrl = "";
            switch (role) {
                case "ADMIN":
                    validationUrl = userManagementUrl + "/admins/profile";
                    break;
                case "DOCTOR":
                    validationUrl = userManagementUrl + "/doctors/profile";
                    break;
                case "LABSTAFF":
                    validationUrl = userManagementUrl + "/labstaffs/profile";
                    break;
                case "PATIENT":
                    validationUrl = userManagementUrl + "/patients/profile";
                    break;
                case "RADIOLOGIST":
                    validationUrl = userManagementUrl + "/radiologists/profile";
                    break;
                case "SUPERADMIN":
                    validationUrl = userManagementUrl + "/superadmins/profile";
                    break;
                default:
                    return null;
            }

            // 4. Call the User Service (Directly!)
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + jwtToken);

            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    validationUrl,
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    String.class);

            // 5. If successful, create and return the User
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                User user = new User();
                user.setType(role);

                String userProfileJson = responseEntity.getBody();
                JsonNode profileNode = objectMapper.readTree(userProfileJson);
                Long userId = profileNode.path("id").asLong();

                user.setId(userId);
                user.setToken(jwtToken);
                return user;
            }

        } catch (Exception e) {
            // Token parsing failed, or Role missing, or Service call failed (403/401)
            e.printStackTrace(); // Optional logging
            return null;
        }

        return null;
    }

    /**
     * Checks if a Recipient actually exists in the system.
     */
    @Override
    public boolean isRecipientValid(String recipientType, Long recipientId, User currentUser) {
        String jwtToken = currentUser.getToken();

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);

        HashMap<String, String> urlMap = new HashMap<>();
        urlMap.put("ADMIN", userManagementUrl + "/admins/validateAdminId/" + recipientId);
        urlMap.put("DOCTOR", userManagementUrl + "/doctors/validateDoctorId/" + recipientId);
        urlMap.put("LABSTAFF", userManagementUrl + "/labstaffs/validateLabStaffId/" + recipientId);
        urlMap.put("PATIENT", userManagementUrl + "/patients/validatePatientId/" + recipientId);
        urlMap.put("RADIOLOGIST", userManagementUrl + "/radiologists/validateRadiologistId/" + recipientId);
        urlMap.put("SUPERADMIN", userManagementUrl + "/superadmins/validateSuperAdminId/" + recipientId);

        String url = urlMap.get(recipientType);
        if (url == null)
            return false;

        ResponseEntity<String> responseEntity;
        try {
            responseEntity = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), String.class);
        } catch (HttpClientErrorException.Forbidden ex) {
            return false;
        }

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return Boolean.parseBoolean(responseEntity.getBody());
        }
        return false;
    }

    /**
     * Checks if a Chat Session exists in the Collaboration Service.
     */
    @Override
    public boolean isChatValid(String chatType, Long chatId, User currentUser) {
        String jwtToken = currentUser.getToken();

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);

        // Call to the Collaboration Service (running on port 9195)
        // Call to the Collaboration Service
        String url = collaborationUrl + "/collaboration/validateMessage/" + chatType + "/" + chatId;

        ResponseEntity<String> responseEntity;
        try {
            responseEntity = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), String.class);
        } catch (RuntimeException e) {
            return false;
        }

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return Boolean.parseBoolean(responseEntity.getBody());
        }
        return false;
    }

    /**
     * Checks if a Consent Request exists in the Consent Service.
     */
    @Override
    public boolean isConsentRequestValid(Long consentRequestId, User currentUser) {
        String jwtToken = currentUser.getToken();

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);

        // Call to the Consent Service (running on port 9202)
        // Call to the Consent Service
        String url = consentUrl + "/consent/validateConsentRequestById/" + consentRequestId;

        ResponseEntity<String> responseEntity;
        try {
            responseEntity = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), String.class);
        } catch (RuntimeException e) {
            return false;
        }

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return Boolean.parseBoolean(responseEntity.getBody());
        }
        return false;
    }
}
