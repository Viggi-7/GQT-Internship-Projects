
// File: src/main/java/com/example/backend/controller/SellerController.java
package com.example.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.dto.PacketDto;
import com.example.backend.dto.ShippingPacketRequest;
import com.example.backend.model.ShippingPacket;
import com.example.backend.service.JwtService;
import com.example.backend.service.SellerService;

import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/seller")
@RequiredArgsConstructor
public class SellerController {

    private final SellerService sellerService;
    private final JwtService jwtService;

    @PostMapping("/packets")
    public ResponseEntity<ShippingPacket> createPacket(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody ShippingPacketRequest request) {
        Long sellerId = getUserIdFromToken(token);
        ShippingPacket newPacket = sellerService.createShippingPacket(request, sellerId);
        return new ResponseEntity<>(newPacket, HttpStatus.CREATED);
    }

    @GetMapping("/packets")
    public ResponseEntity<List<PacketDto>> getMyPackets(@RequestHeader("Authorization") String token) {
        Long sellerId = getUserIdFromToken(token);
        return ResponseEntity.ok(sellerService.getSellerPackets(sellerId));
    }
    
    private Long getUserIdFromToken(String token) {
        // This now works because extractAllClaims is public in JwtService
        String jwt = token.substring(7);
        Claims claims = jwtService.extractAllClaims(jwt);
        // It's safer to convert the number from the claim to Integer first, then to Long
        return ((Number) claims.get("userId")).longValue();
    }
}