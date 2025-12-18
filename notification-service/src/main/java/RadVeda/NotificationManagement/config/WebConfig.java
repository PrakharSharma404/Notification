package RadVeda.NotificationManagement.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
/**
 * WHAT IS THE POINT OF THIS FILE:
 * This is the central Configuration class for the Web Layer (Spring MVC).
 * 
 * WHY WE NEED IT:
 * 1. To Register Argument Resolvers: It tells Spring "Use my
 * CurrentUserArgumentResolver".
 * 2. To Configure CORS: It tells Spring "Allow requests from these domains" (so
 * the browser doesn't block them).
 */
public class WebConfig implements WebMvcConfigurer {

    private final CurrentUserArgumentResolver currentUserArgumentResolver;

    @Value("${cors.allowed-origins}")
    private String[] allowedOrigins;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(currentUserArgumentResolver);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(allowedOrigins)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
