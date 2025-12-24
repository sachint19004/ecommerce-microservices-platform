// Your package name (with the underscore)
package com.ecommerce.user_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.security.config.Customizer.withDefaults;

// @Configuration tells Spring this is a setup class
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // This @Bean creates a PasswordEncoder that we can use anywhere
    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCrypt is the industry standard for hashing passwords
        return new BCryptPasswordEncoder();
    }

    // This @Bean configures our web security rules
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Disable CSRF (we'll use JWT/tokens later)
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authz -> authz
                // Allow anyone to access the /register endpoint
                .requestMatchers("/api/users/register").permitAll()

                // NEW: Allow anyone to access the /login endpoint
                .requestMatchers("/api/users/login").permitAll() 

                // Allow anyone to access our /hello test endpoint
                .requestMatchers("/hello").permitAll()
                // All other requests must be authenticated
                .anyRequest().authenticated()
            )
            .httpBasic(withDefaults()); // Use basic authentication (for now)

        return http.build();
    }
}