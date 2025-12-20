package RadVeda.NotificationManagement.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Enable a simple memory-based message broker to send messages back to the client
        // Clients subscribe to /topic/something
        config.enableSimpleBroker("/topic");
        // Messages sent from client to server start with /app
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // The endpoint the frontend connects to. 
        // setAllowedOriginPatterns("*") is lazy but works for your "AntiGravity" demo.
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS();
    }
}
