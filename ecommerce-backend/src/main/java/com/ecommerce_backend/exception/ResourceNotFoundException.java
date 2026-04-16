package com.ecommerce_backend.exception;

// Custom exception - thrown when something is not found in DB
// Example: Product with ID 99 doesn't exist
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}