package com.ecommerce.product_service;

import com.ecommerce.product_service.dto.ProductResponse; // Import the DTO
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    // --- 1. POST (CREATE) Endpoint ---
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createProduct(@RequestBody Product product) {
        productRepository.save(product);
    }

    // --- 4. NEW: BULK CREATE Endpoint ---
    @PostMapping("/bulk")
    @ResponseStatus(HttpStatus.CREATED)
    @SuppressWarnings("null")
    public void createProducts(@RequestBody List<Product> products) {
        productRepository.saveAll(products);
    }

    // --- 2. GET (READ ALL) Endpoint ---
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // --- 3. REDUCE QUANTITY Endpoint ---
    @PutMapping("/reduce-quantity/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void reduceQuantity(@PathVariable Long id, @RequestParam int quantity) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));

        if (product.getQuantity() == null || product.getQuantity() < quantity) {
            throw new RuntimeException("Not enough stock for Product ID: " + id);
        }

        product.setQuantity(product.getQuantity() - quantity);
        productRepository.save(product);
    } // <--- THIS WAS MISSING!

    // --- 5. NEW: Get Single Product by ID ---
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponse getProductById(@PathVariable Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        return mapToProductResponse(product);
    }

    // Helper method
    private ProductResponse mapToProductResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getQuantity()
        );
    }
}