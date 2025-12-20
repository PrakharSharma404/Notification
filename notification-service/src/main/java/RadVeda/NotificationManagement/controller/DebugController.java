package RadVeda.NotificationManagement.controller;

import RadVeda.NotificationManagement.config.RabbitMQConfig;
import RadVeda.NotificationManagement.consumer.NotificationMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/debug")
@RequiredArgsConstructor
public class DebugController {

    private final RabbitTemplate rabbitTemplate;

    @PostMapping("/fake-event")
    public String sendFakeEvent(@RequestBody NotificationMessage message) {
        // Put a message on the queue manually
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.ROUTING_KEY,
                message);
        return "Event Sent to Queue!";
    }
}
