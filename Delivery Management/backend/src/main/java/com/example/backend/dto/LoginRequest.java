
// File: src/main/java/com/example/backend/dto/LoginRequest.java
package com.example.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LoginRequest {
    @NotBlank @Email String email;
    @NotBlank String password;
}