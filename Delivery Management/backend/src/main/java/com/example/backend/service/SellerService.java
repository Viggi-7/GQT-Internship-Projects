

// File: src/main/java/com/example/backend/service/SellerService.java
package com.example.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.backend.dto.PacketDto;
import com.example.backend.dto.ShippingPacketRequest;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.model.DeliveryBoy;
import com.example.backend.model.PacketStatus;
import com.example.backend.model.Seller;
import com.example.backend.model.ShippingPacket;
import com.example.backend.repository.DeliveryBoyRepository;
import com.example.backend.repository.SellerRepository;
import com.example.backend.repository.ShippingPacketRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SellerService {

    private final ShippingPacketRepository packetRepository;
    private final SellerRepository sellerRepository;
    private final DeliveryBoyRepository deliveryBoyRepository;

    public ShippingPacket createShippingPacket(ShippingPacketRequest request, Long sellerId) {
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new ResourceNotFoundException("Seller not found with ID: " + sellerId));

        ShippingPacket packet = new ShippingPacket();
        packet.setProductName(request.getProductName());
        packet.setProductDescription(request.getProductDescription());
        packet.setDestinationAddress(request.getDestinationAddress());
        packet.setSeller(seller);
        packet.setStatus(PacketStatus.PENDING_ASSIGNMENT);

        if (request.getDeliveryBoyId() != null) {
            DeliveryBoy deliveryBoy = deliveryBoyRepository.findById(request.getDeliveryBoyId())
                    .orElseThrow(() -> new ResourceNotFoundException("Delivery Boy not found with ID: " + request.getDeliveryBoyId()));
            packet.setDeliveryBoy(deliveryBoy);
            packet.setStatus(PacketStatus.OUT_FOR_DELIVERY_METRO); // Or some other initial status
        }

        return packetRepository.save(packet);
    }

    public List<PacketDto> getSellerPackets(Long sellerId) {
        if (!sellerRepository.existsById(sellerId)) {
            throw new ResourceNotFoundException("Seller not found with ID: " + sellerId);
        }
        List<ShippingPacket> packets = packetRepository.findBySellerIdOrderByUpdatedAtDesc(sellerId);
        return packets.stream().map(this::convertToDto).collect(Collectors.toList());
    }
    
    // --- FIX: Changed from private to public ---
    public PacketDto convertToDto(ShippingPacket packet) {
        String deliveryBoyName = packet.getDeliveryBoy() != null ? packet.getDeliveryBoy().getName() : "Unassigned";
        return new PacketDto(
                packet.getId(),
                packet.getProductName(),
                packet.getDestinationAddress(),
                packet.getStatus(),
                packet.getCurrentLatitude(),
                packet.getCurrentLongitude(),
                packet.getUpdatedAt(),
                deliveryBoyName,
                calculateProgress(packet.getStatus())
        );
    }

    private int calculateProgress(PacketStatus status) {
        return switch (status) {
            case PENDING_ASSIGNMENT -> 10;
            case OUT_FOR_DELIVERY_METRO -> 30;
            case REACHED_DISTRICT_HUB -> 50;
            case OUT_FOR_DELIVERY_TOWN -> 75;
            case DELIVERED -> 100;
            case CANCELLED -> 0;
            // A default case is good practice for switch expressions
            default -> 0;
        };
    }
}
