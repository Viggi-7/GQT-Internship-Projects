// File: src/main/java/com/example/backend/dto/PacketDto.java
package com.example.backend.dto;

import java.time.Instant;

import com.example.backend.model.PacketStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class PacketDto {
    private Long id;
    private String productName;
    private String destinationAddress;
    private PacketStatus status;
    private Double currentLatitude;
    private Double currentLongitude;
    private Instant updatedAt;
    private String deliveryBoyName;
    private int progressPercentage;
}