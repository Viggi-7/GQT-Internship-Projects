
// File: src/main/java/com/example/backend/service/DeliveryService.java
package com.example.backend.service;

import com.example.backend.dto.LocationUpdateRequest;
import com.example.backend.dto.PacketDto;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.model.ShippingPacket;
import com.example.backend.repository.DeliveryBoyRepository;
import com.example.backend.repository.ShippingPacketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeliveryService {
    
    private final ShippingPacketRepository packetRepository;
    private final DeliveryBoyRepository deliveryBoyRepository;
    private final SellerService sellerService; // Reuse DTO conversion and progress logic

    public List<PacketDto> getDeliveryBoyPackets(Long deliveryBoyId) {
        if (!deliveryBoyRepository.existsById(deliveryBoyId)) {
            throw new ResourceNotFoundException("Delivery Boy not found with ID: " + deliveryBoyId);
        }
        List<ShippingPacket> packets = packetRepository.findByDeliveryBoyIdOrderByUpdatedAtDesc(deliveryBoyId);
        // Use the same conversion logic from SellerService to keep things DRY
        return packets.stream().map(sellerService::convertToDto).collect(Collectors.toList());
    }

    public PacketDto updatePacketLocation(Long packetId, LocationUpdateRequest request) {
        ShippingPacket packet = packetRepository.findById(packetId)
                .orElseThrow(() -> new ResourceNotFoundException("Packet not found with ID: " + packetId));
        
        packet.setCurrentLatitude(request.getLatitude());
        packet.setCurrentLongitude(request.getLongitude());
        packet.setStatus(request.getNewStatus());
        
        ShippingPacket updatedPacket = packetRepository.save(packet);
        
        return sellerService.convertToDto(updatedPacket);
    }
}
