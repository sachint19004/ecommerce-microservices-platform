package com.ecommerce.user_service.service; // Or com.ecommerce.userservice.service

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    private final String SECRET_KEY = "THISISASECRETAUTHORIZATIONKEY256"; 
// Note: 32 characters long
    private final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 hour

    public String generateToken(String email) {
        
        // Get the secret key
        Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

        // Get the current time and expiration time
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);

        // --- THIS IS THE UPDATED CODE ---
        return Jwts.builder()
            .subject(email)                 // Replaces .setSubject()
            .issuedAt(now)                  // Replaces .setIssuedAt()
            .expiration(expiryDate)         // Replaces .setExpiration()
            .signWith(key)                  // Replaces .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }
}