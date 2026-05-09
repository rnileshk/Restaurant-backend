package com.restaurant.app.service;

import com.restaurant.app.entity.Delivery;
import com.restaurant.app.entity.DeliveryStatus;

import com.restaurant.app.repository.DeliveryRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;

    public DeliveryService(
            DeliveryRepository deliveryRepository
    ) {
        this.deliveryRepository = deliveryRepository;
    }

    /*
     =====================================
     ASSIGN DELIVERY
     =====================================
    */

    public Delivery assignDelivery(
            Delivery delivery
    ) {

        delivery.setStatus(
                DeliveryStatus.PENDING
        );

        if (delivery.getAssignedAt() == null) {
            delivery.setAssignedAt(
                    LocalDateTime.now()
            );
        }

        return deliveryRepository.save(delivery);
    }

    /*
     =====================================
     UPDATE DELIVERY STATUS
     =====================================
    */

    public Delivery updateStatus(
            Long id,
            DeliveryStatus status
    ) {

        Delivery delivery =
                getDeliveryById(id);

        delivery.setStatus(status);

        /*
         =====================================
         AUTO SET DELIVERED TIME
         =====================================
        */

        if (status == DeliveryStatus.DELIVERED) {

            delivery.setDeliveredAt(
                    LocalDateTime.now()
            );
        }

        return deliveryRepository.save(delivery);
    }

    /*
     =====================================
     GET ALL DELIVERIES
     =====================================
    */

    public List<Delivery> getAllDeliveries() {

        return deliveryRepository.findAll();
    }

    /*
     =====================================
     GET DELIVERY BY ID
     =====================================
    */

    public Delivery getDeliveryById(
            Long id
    ) {

        return deliveryRepository.findById(id)
                .orElseThrow(() ->

                        new RuntimeException(
                                "Delivery not found"
                        )
                );
    }

    /*
     =====================================
     DELETE DELIVERY
     =====================================
    */

    public void deleteDelivery(
            Long id
    ) {

        Delivery delivery =
                getDeliveryById(id);

        deliveryRepository.delete(delivery);
    }
}