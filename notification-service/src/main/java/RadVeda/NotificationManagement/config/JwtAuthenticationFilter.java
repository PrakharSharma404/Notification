package RadVeda.NotificationManagement.config;

import RadVeda.NotificationManagement.NotificationService;
import RadVeda.NotificationManagement.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
@RequiredArgsConstructor
/**
 * WHAT IS THE POINT OF THIS FILE:
 * This is the "Bouncer" at the door. It intercepts every request to check for a
 * valid ID badge (JWT).
 *
 * WHY WE NEED IT:
 * Standard Spring Security doesn't know about our custom JWT format or external
 * validation logic.
 * This filter:
 * 1. Grabs the "Authorization" header.
 * 2. Asks the NotificationService to validate it (which calls the other
 * microservices).
 * 3. If valid, it registers the User in the `SecurityContext`.
 * This effectively "Logs In" the user for the duration of this request.
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final NotificationService notificationService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        // 1. Check if token is present
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Validate Token via our Service (Result is a User object if valid,
        // null/exception if not)
        try {
            User user = notificationService.authenticate(authHeader);

            if (user != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // 3. Create Authentication Token
                // Note: We use an empty list for authorities for now, but roles could be mapped
                // here.
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        user, null, new ArrayList<>());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 4. Set the Authentication in the Context
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        } catch (Exception e) {
            // If validation fails, we just don't set the authentication.
            // Spring Security will handle the 403/401 later since the context is empty.
        }

        filterChain.doFilter(request, response);
    }
}
