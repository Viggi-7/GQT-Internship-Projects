

// File: src/main/java/com/example/backend/dto/AuthResponse.java
package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public class AuthResponse {
    private String token;
    private String userType;
    private Long userId;
    private String name;
}
