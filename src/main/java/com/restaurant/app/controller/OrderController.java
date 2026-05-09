package com.restaurant.app.controller;

import com.restaurant.app.entity.DeliveryStatus;
import com.restaurant.app.entity.FoodOrder;
import com.restaurant.app.entity.OrderStatus;
import com.restaurant.app.service.OrderService;
import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin("*")
public class OrderController {

    private final OrderService orderService;

    public OrderController(
            OrderService orderService
    ) {
        this.orderService = orderService;
    }

    @PostMapping
    public FoodOrder createOrder(
            @RequestBody FoodOrder order,
            Authentication authentication
    ) {
        return orderService.createOrder(order, authentication.getName());
    }

    @GetMapping
    public List<FoodOrder> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public FoodOrder getOrderById(
            @PathVariable Long id
    ) {
        return orderService.getOrderById(id);
    }

    @PutMapping("/{id}/status")
    public FoodOrder updateStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus status
    ) {
        return orderService.updateStatus(id, status);
    }

    @PutMapping("/{id}/delivery-status")
    public FoodOrder updateDeliveryStatus(
            @PathVariable Long id,
            @RequestParam DeliveryStatus status
    ) {
        return orderService.updateDeliveryStatus(id, status);
    }

    @DeleteMapping("/{id}")
    public String deleteOrder(
            @PathVariable Long id
    ) {
        orderService.deleteOrder(id);
        return "Order deleted successfully";
    }

    @GetMapping("/my")
    public List<FoodOrder> getMyOrders(
        Authentication authentication
    ) {
        return orderService.getMyOrders(authentication.getName());
    }
}