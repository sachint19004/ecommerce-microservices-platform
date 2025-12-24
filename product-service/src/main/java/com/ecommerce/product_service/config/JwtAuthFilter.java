package com.ecommerce.product_service.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull; // Import for @NonNull
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    // 🔑 Key must match User Service. 
    // Ensuring we use standard charset to avoid encoding issues.
    private final String SECRET_KEY = "THISISASECRETAUTHORIZATIONKEY256"; 
    private final SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,     // Added @NonNull
            @NonNull HttpServletResponse response,    // Added @NonNull
            @NonNull FilterChain filterChain          // Added @NonNull
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        String jwt = null;
        String userEmail = null;

        // 1. Check if the token is present
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            
            try {
                // 2. Validate token (Updated for JJWT 0.12.3 syntax)
                Claims claims = Jwts.parser()                 // Changed from parserBuilder()
                        .verifyWith(key)                      // Changed from setSigningKey()
                        .build()
                        .parseSignedClaims(jwt)               // Changed from parseClaimsJws()
                        .getPayload();                        // Changed from getBody()

                userEmail = claims.getSubject();
            } catch (Exception e) {
                // Token is invalid/expired
                // Use the logger inherited from GenericFilterBean
                logger.warn("JWT Token validation failed: " + e.getMessage());
            }
        }

        // 3. Authenticate if valid
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userEmail, 
                    null,      
                    Collections.emptyList()
            );
            
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        filterChain.doFilter(request, response);
    }
}