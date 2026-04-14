// File: src/main/java/com/example/backend/dto/RegisterRequest.java
package com.example.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RegisterRequest {
    @NotBlank String name;
    @NotBlank @Email String email;
    @NotBlank @Size(min = 6) String password;
    String phoneNumber;
}
