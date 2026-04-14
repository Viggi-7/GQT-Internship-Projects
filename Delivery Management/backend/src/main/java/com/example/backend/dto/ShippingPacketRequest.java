
// File: src/main/java/com/example/backend/dto/ShippingPacketRequest.java
package com.example.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ShippingPacketRequest {
    @NotBlank String productName;
    String productDescription;
    @NotBlank String destinationAddress;
    Long deliveryBoyId;
}
