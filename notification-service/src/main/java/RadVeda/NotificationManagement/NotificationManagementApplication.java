package RadVeda.NotificationManagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * WHAT IT IS:
 * This is the main entry point for the Notification Management Service.
 * 
 * WHY WE NEED IT:
 * Every Spring Boot application needs a main class to bootstrap the
 * application.
 * It sets up the Spring application context, which manages all the beans and
 * dependencies
 * of our application.
 * 
 * WHY WE NEED IT ALL OF THAT:
 * The @SpringBootApplication annotation is a convenience annotation that adds:
 * 1. @Configuration: Tags the class as a source of bean definitions for the
 * application context.
 * 2. @EnableAutoConfiguration: Tells Spring Boot to start adding beans based on
 * classpath settings,
 * other beans, and various property settings. For example, if 'spring-webmvc'
 * is on the classpath,
 * this flag flags the application as a web application and activates key
 * behaviors like setting up
 * a DispatcherServlet.
 * 3. @ComponentScan: Tells Spring to look for other components, configurations,
 * and services in the
 * 'RadVeda.NotificationManagement' package, letting it find the controllers and
 * services we write.
 */
@SpringBootApplication
public class NotificationManagementApplication {

    public static void main(String[] args) {
        // This line launches the application. It creates the ApplicationContext and
        // starts the embedded server (Tomcat by default).
        SpringApplication.run(NotificationManagementApplication.class, args);
    }

}
