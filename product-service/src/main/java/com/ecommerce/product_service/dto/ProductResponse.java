package com.ecommerce.product_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
// import java.math.BigDecimal; // Remove this if you want, or just leave it

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    
    private Double price; // <--- CHANGED FROM BigDecimal TO Double
    
    private Integer quantity;
}