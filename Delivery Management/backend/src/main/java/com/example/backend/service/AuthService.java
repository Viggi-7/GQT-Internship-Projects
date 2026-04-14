// File: src/main/java/com/example/backend/service/AuthService.java
package com.example.backend.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.backend.dto.AuthResponse;
import com.example.backend.dto.LoginRequest;
import com.example.backend.dto.RegisterRequest;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.model.DeliveryBoy;
import com.example.backend.model.Seller;
import com.example.backend.repository.DeliveryBoyRepository;
import com.example.backend.repository.SellerRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final SellerRepository sellerRepository;
    private final DeliveryBoyRepository deliveryBoyRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse registerSeller(RegisterRequest request) {
        var seller = new Seller();
        seller.setName(request.getName());
        seller.setEmail(request.getEmail());
        seller.setPassword(passwordEncoder.encode(request.getPassword()));
        var savedSeller = sellerRepository.save(seller);

        var userDetails = org.springframework.security.core.userdetails.User
                .withUsername(savedSeller.getEmail())
                .password(savedSeller.getPassword())
                .roles("SELLER")
                .build();

        var jwtToken = jwtService.generateToken(userDetails, "SELLER", savedSeller.getId());
        return new AuthResponse(jwtToken, "SELLER", savedSeller.getId(), savedSeller.getName());
    }

    public AuthResponse registerDeliveryBoy(RegisterRequest request) {
        var deliveryBoy = new DeliveryBoy();
        deliveryBoy.setName(request.getName());
        deliveryBoy.setEmail(request.getEmail());
        deliveryBoy.setPassword(passwordEncoder.encode(request.getPassword()));
        deliveryBoy.setPhoneNumber(request.getPhoneNumber());
        var savedBoy = deliveryBoyRepository.save(deliveryBoy);

        var userDetails = org.springframework.security.core.userdetails.User
                .withUsername(savedBoy.getEmail())
                .password(savedBoy.getPassword())
                .roles("DELIVERY_BOY")
                .build();
        
        var jwtToken = jwtService.generateToken(userDetails, "DELIVERY_BOY", savedBoy.getId());
        return new AuthResponse(jwtToken, "DELIVERY_BOY", savedBoy.getId(), savedBoy.getName());
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        // Find user in either repository
        var sellerOptional = sellerRepository.findByEmail(request.getEmail());
        if (sellerOptional.isPresent()) {
            Seller seller = sellerOptional.get();
            var userDetails = org.springframework.security.core.userdetails.User
                .withUsername(seller.getEmail()).password(seller.getPassword()).roles("SELLER").build();
            var jwtToken = jwtService.generateToken(userDetails, "SELLER", seller.getId());
            return new AuthResponse(jwtToken, "SELLER", seller.getId(), seller.getName());
        }

        var deliveryBoyOptional = deliveryBoyRepository.findByEmail(request.getEmail());
        if (deliveryBoyOptional.isPresent()) {
            DeliveryBoy boy = deliveryBoyOptional.get();
             var userDetails = org.springframework.security.core.userdetails.User
                .withUsername(boy.getEmail()).password(boy.getPassword()).roles("DELIVERY_BOY").build();
            var jwtToken = jwtService.generateToken(userDetails, "DELIVERY_BOY", boy.getId());
            return new AuthResponse(jwtToken, "DELIVERY_BOY", boy.getId(), boy.getName());
        }
        
        throw new ResourceNotFoundException("User not found");
    }
}