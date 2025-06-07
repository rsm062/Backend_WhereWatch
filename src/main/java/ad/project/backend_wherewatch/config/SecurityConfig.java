package ad.project.backend_wherewatch.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

/**
 * Security configuration class that sets up the HTTP security settings for the application.
 * <p>
 * Currently, it disables CSRF protection, allows all requests without authentication,
 * configures stateless session management, and enables HTTP Basic authentication.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Defines the security filter chain to configure HTTP security.
     *
     * @param http the HttpSecurity to modify
     * @return the configured SecurityFilterChain
     * @throws Exception if an error occurs while configuring security
     */
    @Bean
    public SecurityFilterChain filterChain(org.springframework.security.config.annotation.web.builders.HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
