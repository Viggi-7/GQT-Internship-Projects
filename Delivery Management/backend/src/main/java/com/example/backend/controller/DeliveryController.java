

// File: src/main/java/com/example/backend/controller/DeliveryController.java
package com.example.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.dto.LocationUpdateRequest;
import com.example.backend.dto.PacketDto;
import com.example.backend.service.DeliveryService;
import com.example.backend.service.JwtService;

import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/delivery")
@RequiredArgsConstructor
public class DeliveryController {
    
    private final DeliveryService deliveryService;
    private final JwtService jwtService;

    @GetMapping("/packets")
    public ResponseEntity<List<PacketDto>> getMyAssignedPackets(@RequestHeader("Authorization") String token) {
        Long deliveryBoyId = getUserIdFromToken(token);
        return ResponseEntity.ok(deliveryService.getDeliveryBoyPackets(deliveryBoyId));
    }
    
    @PatchMapping("/packets/{packetId}/location")
    public ResponseEntity<PacketDto> updatePacketLocation(
            @PathVariable Long packetId,
            @Valid @RequestBody LocationUpdateRequest request) {
        return ResponseEntity.ok(deliveryService.updatePacketLocation(packetId, request));
    }

    private Long getUserIdFromToken(String token) {
        // This now works because extractAllClaims is public in JwtService
        String jwt = token.substring(7);
        Claims claims = jwtService.extractAllClaims(jwt);
         // It's safer to convert the number from the claim to Integer first, then to Long
        return ((Number) claims.get("userId")).longValue();
    }
}
