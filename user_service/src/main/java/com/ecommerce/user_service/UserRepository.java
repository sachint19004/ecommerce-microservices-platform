package com.ecommerce.user_service;


import org.springframework.data.jpa.repository.JpaRepository;

// This is an interface, not a class
public interface UserRepository extends JpaRepository<User, Long> {

    // Spring Data JPA will automatically create a method for us
    // just because we named it "findByEmail"
    // Example: findByEmail("test@example.com")
    User findByEmail(String email);

}