package RadVeda.NotificationManagement.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
/**
 * WHAT IS THE POINT OF THIS FILE:
 * This is the Brain of our Security System.
 *
 * WHY WE NEED IT:
 * It tells Spring Security:
 * 1. "Don't use sessions (Cookies)" -> use Stateless (JWT).
 * 2. "Don't enable CSRF" -> because we are an API, not a browser form app.
 * 3. "Use my Custom Filter" -> JwtAuthenticationFilter.
 * 4. "Protect Everything" -> .anyRequest().authenticated().
 */
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for stateless APIs
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().authenticated() // Protect ALL endpoints
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // No sessions
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // Add our JWT
                                                                                                       // filter

        return http.build();
    }
}
