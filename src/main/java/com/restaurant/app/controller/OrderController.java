package com.restaurant.app.controller;

import com.restaurant.app.entity.DeliveryStatus;
import com.restaurant.app.entity.FoodOrder;
import com.restaurant.app.entity.OrderStatus;
import com.restaurant.app.service.OrderService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<FoodOrder> createOrder(
            @RequestBody FoodOrder order,
            Authentication authentication
    ) {
        String email = authentication != null ? authentication.getName() : null;

        FoodOrder savedOrder = orderService.createOrder(order, email);

        return ResponseEntity.ok(savedOrder);
    }

    @GetMapping
    public ResponseEntity<List<FoodOrder>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/my")
    public ResponseEntity<List<FoodOrder>> getMyOrders(Authentication authentication) {

        String email = authentication != null ? authentication.getName() : null;

        return ResponseEntity.ok(orderService.getMyOrders(email));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FoodOrder> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<FoodOrder> updateStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus status
    ) {
        return ResponseEntity.ok(orderService.updateStatus(id, status));
    }

    @PutMapping("/{id}/delivery-status")
    public ResponseEntity<FoodOrder> updateDeliveryStatus(
            @PathVariable Long id,
            @RequestParam DeliveryStatus status
    ) {
        return ResponseEntity.ok(orderService.updateDeliveryStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);

        return ResponseEntity.ok("Order deleted successfully");
    }
}