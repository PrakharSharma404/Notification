package RadVeda.NotificationManagement.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
/**
 * WHAT IS THE POINT OF THIS FILE:
 * Another custom "Marker Annotation", but for Method Parameters.
 * 
 * WHY WE NEED IT:
 * We put this before a `User` argument in a controller method.
 * Example: `public void foo(@CurrentUser User user)`
 * It tells the `CurrentUserArgumentResolver`: "Hey, find the logged-in user and
 * put it here."
 */
public @interface CurrentUser {
}
