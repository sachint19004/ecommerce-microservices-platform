package com.ecommerce.product_service.config;

import org.springframework.beans.factory.annotation.Autowired; // NEW import
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter; // NEW import

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // NEW: Inject the filter we just created
    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            
            // Session management must be STATELESS for JWTs
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            .authorizeHttpRequests(authz -> authz
    .requestMatchers("/hello").permitAll()
    
    // NEW: Allow everyone to see and add products without a token
    .requestMatchers("/api/products/**").permitAll() 
    
    .anyRequest().authenticated()
);
            
        // NEW: Add our JWT filter BEFORE the standard Spring Security filter
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
            
        return http.build();
    }
}