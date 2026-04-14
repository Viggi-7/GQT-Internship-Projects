
// File: src/main/java/com/example/backend/exception/ResourceNotFoundException.java
package com.example.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception that results in a 404 Not Found HTTP response.
 * This is a clean way to handle "not found" scenarios in the service layer.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
