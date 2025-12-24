package com.ecommerce.order_service.client;

import com.ecommerce.order_service.dto.ProductDto; // 1. Add this Import
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping; // Import GetMapping
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "product-service", url = "http://localhost:8081")
public interface ProductClient {

    @PutMapping("/api/products/reduce-quantity/{id}")
    void reduceQuantity(@PathVariable("id") long id, @RequestParam("quantity") int quantity);

    // --- 2. Add this NEW Method ---
    @GetMapping("/api/products/{id}")
    ProductDto getProductById(@PathVariable("id") Long id);
}