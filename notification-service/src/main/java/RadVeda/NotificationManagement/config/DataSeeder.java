package RadVeda.NotificationManagement.config;

import RadVeda.NotificationManagement.Notifications.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final ChatNotificationRepository chatNotificationRepository;
    private final ConsentRequestNotificationRepository consentRequestNotificationRepository;
    private final OneWayNotificationRepository oneWayNotificationRepository;

    @Override
    public void run(String... args) throws Exception {
        if (chatNotificationRepository.count() == 0) {
            ChatNotification chat1 = new ChatNotification();
            chat1.setMessage("Hello! Is my report ready?");
            chat1.setRecipientId(1L);
            chat1.setRecipientType("PATIENT");
            chat1.setChatType("PRIVATE");
            chat1.setChatId(101L);
            chatNotificationRepository.save(chat1);

            ChatNotification chat2 = new ChatNotification();
            chat2.setMessage("Please schedule follow-up.");
            chat2.setRecipientId(2L);
            chat2.setRecipientType("DOCTOR");
            chat2.setChatType("GROUP");
            chat2.setChatId(102L);
            chatNotificationRepository.save(chat2);
        }

        if (consentRequestNotificationRepository.count() == 0) {
            ConsentRequestNotification consent1 = new ConsentRequestNotification();
            consent1.setMessage("Dr. Smith requests access to your MRI records.");
            consent1.setRecipientId(1L);
            consent1.setRecipientType("PATIENT");
            consent1.setConsentRequestId(501L);
            consentRequestNotificationRepository.save(consent1);
        }

        if (oneWayNotificationRepository.count() == 0) {
            OneWayNotification oneWay1 = new OneWayNotification();
            oneWay1.setMessage("System maintenance scheduled for tonight.");
            oneWay1.setRecipientId(1L);
            oneWay1.setRecipientType("PATIENT");
            oneWayNotificationRepository.save(oneWay1);
        }
    }
}
