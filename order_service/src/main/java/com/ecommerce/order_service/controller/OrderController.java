package com.ecommerce.order_service.controller;

import com.ecommerce.order_service.dto.OrderRequest;
import com.ecommerce.order_service.service.OrderService;
import com.ecommerce.order_service.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity; // Changed to ResponseEntity for better error feedback
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final JwtUtils jwtUtils;

    @PostMapping
    public ResponseEntity<String> placeOrder(@RequestBody OrderRequest orderRequest, 
                                             @RequestHeader(value = "Authorization", required = false) String authHeader) {
        
        // 1. Check if Header is present
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                 .body("Error: Missing or malformed Authorization header. Use 'Bearer <token>'");
        }

        try {
            // 2. Extract Token
            String token = authHeader.substring(7);

            // 3. Extract Subject (Should be User ID)
            String secureUserId = jwtUtils.extractUsername(token);
            
            // LOG THIS: Check your console to see what is actually inside the token
            System.out.println("DEBUG: Extracted Subject from Token: " + secureUserId);

            // 4. Validate if it's a number (Prevents NumberFormatException)
            try {
                Long userId = Long.parseLong(secureUserId);
                orderRequest.setUserId(userId);
            } catch (NumberFormatException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                     .body("Error: Token contains an Email [" + secureUserId + "], but Order Service needs a Numeric ID.");
            }

            // 5. Place Order
            orderService.placeOrder(orderRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body("Order Placed Successfully");

        } catch (Exception e) {
            // This catches Signature issues, Expired tokens, etc.
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                 .body("Error: Token validation failed: " + e.getMessage());
        }
    }
}