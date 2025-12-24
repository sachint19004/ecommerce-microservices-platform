package com.ecommerce.user_service; // Or com.ecommerce.user_service

import com.ecommerce.user_service.dto.LoginRequest;
import com.ecommerce.user_service.service.JwtService; // NEW Import
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // NEW: Inject the JwtService
    @Autowired
    private JwtService jwtService;

    @PostMapping("/register")
    public User registerUser(@Valid @RequestBody User user) {

        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);

        return userRepository.save(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequest loginRequest) {

        User user = userRepository.findByEmail(loginRequest.getEmail());

        if (user != null && passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {

            // --- THIS IS THE NEW PART ---
            // 1. Generate the token
            String token = jwtService.generateToken(user.getEmail());

            // 2. Create a map to hold the token
            Map<String, String> response = new HashMap<>();
            response.put("token", token);

            // 3. Return the token in the response
            return ResponseEntity.ok(response);
        }

        // If user not found or password incorrect
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Invalid email or password");

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }
}