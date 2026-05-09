package com.restaurant.app.controller;

import com.restaurant.app.entity.Delivery;
import com.restaurant.app.entity.DeliveryStatus;
import com.restaurant.app.repository.DeliveryRepository;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/delivery")
@CrossOrigin("*")
public class DeliveryController {

    private final DeliveryRepository deliveryRepository;

    public DeliveryController(
            DeliveryRepository deliveryRepository
    ) {
        this.deliveryRepository = deliveryRepository;
    }

    @PostMapping
    public Delivery createDelivery(
            @RequestBody Delivery delivery
    ) {
        return deliveryRepository.save(delivery);
    }

    @GetMapping
    public List<Delivery> getAllDelivery() {
        return deliveryRepository.findAll();
    }

    @PutMapping("/{id}/status")
    public Delivery updateStatus(
            @PathVariable Long id,
            @RequestParam DeliveryStatus status
    ) {

        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow();

        delivery.setStatus(status);

        return deliveryRepository.save(delivery);
    }
}