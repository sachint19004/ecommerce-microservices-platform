package com.ecommerce.product_service; // Ensure your package name matches!

import org.springframework.data.jpa.repository.JpaRepository;

// This interface inherits all CRUD methods (save, findAll, delete, etc.)
// We specify the Entity (Product) and the type of its primary key (Long)
public interface ProductRepository extends JpaRepository<Product, Long> {

    // No methods needed here for now! JpaRepository handles everything.
}