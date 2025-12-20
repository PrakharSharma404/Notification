package RadVeda.NotificationManagement.consumer;

import RadVeda.NotificationManagement.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import RadVeda.NotificationManagement.consumer.NotificationMessage;

@Component
@Slf4j
@RequiredArgsConstructor
public class NotificationConsumer {

    private final NotificationService notificationService;

    // Listen to the queue defined in Config
    @RabbitListener(queues = "notification_queue")
    public void receiveMessage(NotificationMessage message) {
        log.info("Received event: {}", message);

        // Switch based on type (Cheap way to handle polymorphism in JSON)
        switch (message.getType()) {
            case "CHAT":
                notificationService.processChatEvent(message);
                break;
            case "CONSENT":
                notificationService.processConsentEvent(message);
                break;
            case "ONE_WAY":
                notificationService.processOneWayEvent(message);
                break;
            default:
                log.warn("Unknown message type: {}", message.getType());
        }
    }
}
