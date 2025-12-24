package com.ecommerce.user_service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class testcontrol 
{
    // This maps the function to the URL "/hello"
    @GetMapping("/hello")
    public String sayHello() {
        // Spring will return this text as the webpage content
        return "Hello from the User Service!";
    }
}
