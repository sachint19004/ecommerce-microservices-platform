// Note the package name is now ".exception"
package com.ecommerce.user_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

// @ControllerAdvice tells Spring this class will handle exceptions globally
@ControllerAdvice
public class GlobalExceptionHandler
 {

    // This method will run *only* when a @Valid annotation fails
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException ex) 
    {
        
        // We will create a simple map to hold our error message
        Map<String, String> errors = new HashMap<>();

        // Get the *first* validation error message
        String firstErrorMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        
        // Put it in our map
        errors.put("error", firstErrorMessage);

        // Return a 400 Bad Request with the clean error map
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}