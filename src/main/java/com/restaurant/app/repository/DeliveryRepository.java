package com.restaurant.app.repository;

import com.restaurant.app.entity.Delivery;
import com.restaurant.app.entity.DeliveryStatus;
import com.restaurant.app.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeliveryRepository
        extends JpaRepository<Delivery, Long> {

    List<Delivery> findByDeliveryBoy(
            User deliveryBoy
    );

    List<Delivery> findByStatus(
            DeliveryStatus status
    );
}