package com.restaurant.app.repository;

import com.restaurant.app.entity.FoodOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<FoodOrder, Long> {

    List<FoodOrder> findByUserEmail(String email);
}