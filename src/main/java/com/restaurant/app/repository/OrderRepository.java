package com.restaurant.app.repository;

import com.restaurant.app.entity.FoodOrder;
import com.restaurant.app.entity.OrderStatus;
import com.restaurant.app.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository
        extends JpaRepository<FoodOrder, Long> {

    List<FoodOrder> findByUser(
            User user
    );

    List<FoodOrder> findByUserEmail(
        String email
     );

    List<FoodOrder> findByStatus(
            OrderStatus status
    );

    List<FoodOrder> findByOrderCode(
            String orderCode
    );
}