

// File: src/main/java/com/example/backend/service/CustomUserDetailsService.java
package com.example.backend.service;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.backend.repository.DeliveryBoyRepository;
import com.example.backend.repository.SellerRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final SellerRepository sellerRepository;
    private final DeliveryBoyRepository deliveryBoyRepository;

    public CustomUserDetailsService(SellerRepository sellerRepository, DeliveryBoyRepository deliveryBoyRepository) {
        this.sellerRepository = sellerRepository;
        this.deliveryBoyRepository = deliveryBoyRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // First, try to find a seller with the given email
        return sellerRepository.findByEmail(email)
            .map(seller -> new User(
                seller.getEmail(),
                seller.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_SELLER"))
            ))
            // If not found, try to find a delivery boy
            .orElseGet(() -> deliveryBoyRepository.findByEmail(email)
                .map(deliveryBoy -> new User(
                    deliveryBoy.getEmail(),
                    deliveryBoy.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_DELIVERY_BOY"))
                ))
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email))
            );
    }
}

