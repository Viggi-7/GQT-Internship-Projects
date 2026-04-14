


// File: src/main/java/com/example/backend/dto/LocationUpdateRequest.java
package com.example.backend.dto;

import com.example.backend.model.PacketStatus;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LocationUpdateRequest {
    @NotNull Double latitude;
    @NotNull Double longitude;
    @NotNull PacketStatus newStatus;
}
