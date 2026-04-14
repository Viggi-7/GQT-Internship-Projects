
// File: src/main/java/com/example/backend/repository/ShippingPacketRepository.java
package com.example.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.backend.model.ShippingPacket;

@Repository
public interface ShippingPacketRepository extends JpaRepository<ShippingPacket, Long> {
    List<ShippingPacket> findBySellerIdOrderByUpdatedAtDesc(Long sellerId);
    List<ShippingPacket> findByDeliveryBoyIdOrderByUpdatedAtDesc(Long deliveryBoyId);
}
