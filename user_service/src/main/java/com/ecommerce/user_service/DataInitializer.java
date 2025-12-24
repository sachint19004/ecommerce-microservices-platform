package com.ecommerce.user_service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import java.util.Optional; // NEW: Added Optional import for best practice

@Configuration
public class DataInitializer {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);
    
    // Define the credentials we will use for testing
    private static final String TEST_PASSWORD = "password123";
    private static final String TEST_EMAIL = "debug@test.com";

    @Bean
    public CommandLineRunner initDatabase(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            
            // Check if the user already exists (for persistence)
            // Note: FindByEmail returns a User, not an Optional, but checking against null is fine here
            if (userRepository.findByEmail(TEST_EMAIL) == null) {
                
                User debugUser = new User();
                debugUser.setEmail(TEST_EMAIL);
                
                // CRITICAL FIX: Setting required fields for validation
                debugUser.setFirstName("Debug");
                debugUser.setLastName("User");
                
                String hashedPassword = passwordEncoder.encode(TEST_PASSWORD);
                debugUser.setPassword(hashedPassword);
                
                userRepository.save(debugUser);
                
                // --- CRITICAL DEBUGGING OUTPUT ---
                log.info("--- DEBUG: HASHING CHECK ---");
                log.info("Registered User: {}", TEST_EMAIL);
                log.info("Original Password: {}", TEST_PASSWORD);
                log.info("SAVED HASH: {}", hashedPassword);
                log.info("BCrypt Check: {}", passwordEncoder.matches(TEST_PASSWORD, hashedPassword) ? "SUCCESS!" : "FAILURE!");
                log.info("--- END HASHING CHECK ---");
            }
        };
    }
}