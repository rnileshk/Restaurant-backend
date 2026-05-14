package com.restaurant.app.service;

import com.restaurant.app.entity.*;
import com.restaurant.app.repository.OrderRepository;
import com.restaurant.app.repository.UserRepository;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public OrderService(
            OrderRepository orderRepository,
            UserRepository userRepository
    ) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public FoodOrder createOrder(FoodOrder order, String email) {
        if (order == null) {
            throw new RuntimeException("Order data is missing");
        }

        if (email != null && !email.isBlank()) {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            order.setUser(user);
        }

        if (order.getOrderCode() == null || order.getOrderCode().isBlank()) {
            order.setOrderCode(
                    "ORD-" + UUID.randomUUID()
                            .toString()
                            .substring(0, 8)
                            .toUpperCase()
            );
        }

        if (order.getStatus() == null) {
            order.setStatus(OrderStatus.PENDING);
        }

        if (order.getDeliveryStatus() == null) {
            order.setDeliveryStatus(DeliveryStatus.PENDING);
        }

        double totalAmount = 0.0;

        if (order.getItems() != null && !order.getItems().isEmpty()) {
            for (OrderItem item : order.getItems()) {
                item.setOrder(order);

                if (item.getQuantity() == null || item.getQuantity() <= 0) {
                    item.setQuantity(1);
                }

                if (
                        item.getMenuItem() != null &&
                        item.getMenuItem().getPrice() != null
                ) {
                    item.setPrice(item.getMenuItem().getPrice());
                    item.setName(item.getMenuItem().getName());
                }

                double price = item.getPrice() != null ? item.getPrice() : 0.0;
                double itemTotal = price * item.getQuantity();

                item.setTotalPrice(itemTotal);
                totalAmount += itemTotal;
            }
        }

        if (order.getTotalAmount() == null || order.getTotalAmount() <= 0) {
            order.setTotalAmount(totalAmount);
        }

        FoodOrder savedOrder = orderRepository.save(order);
        orderRepository.flush();

        return savedOrder;
    }

        public List<FoodOrder> getAllOrders() {
                return orderRepository.findAll();
        }

        public List<FoodOrder> getMyOrders(String email) {

        if (email == null || email.isBlank()) {
                return Collections.emptyList();
        }

        return orderRepository.findAll()
            .stream()
            .filter(order ->
                    order.getUser() != null &&
                    order.getUser().getEmail() != null &&
                    order.getUser().getEmail().equals(email)
            )
            .toList();
        }

    public FoodOrder getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public FoodOrder updateStatus(Long id, OrderStatus status) {
        FoodOrder order = getOrderById(id);
        order.setStatus(status);

        return orderRepository.save(order);
    }

    public FoodOrder updateDeliveryStatus(Long id, DeliveryStatus status) {
        FoodOrder order = getOrderById(id);
        order.setDeliveryStatus(status);

        return orderRepository.save(order);
    }

    public void deleteOrder(Long id) {
        FoodOrder order = getOrderById(id);
        orderRepository.delete(order);
    }
}