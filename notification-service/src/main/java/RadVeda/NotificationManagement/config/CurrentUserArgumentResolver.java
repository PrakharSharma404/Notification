package RadVeda.NotificationManagement.config;

import RadVeda.NotificationManagement.User;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
/**
 * WHAT IS THE POINT OF THIS FILE:
 * This class is the "Injector". It works with `@CurrentUser`.
 * 
 * WHY WE NEED IT:
 * Spring doesn't know how to fill a `User` object argument by default.
 * This class tells Spring:
 * 1. "I handle parameters marked with @CurrentUser."
 * 2. "To get the value, look in the Request Attributes for
 * 'authenticatedUser'."
 * (which was put there by the AuthenticationInterceptor).
 */
public class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(CurrentUser.class) != null &&
                parameter.getParameterType().equals(User.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        // With Spring Security, the Principal is now our User object!
        return org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
    }
}
