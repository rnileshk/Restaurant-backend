package com.restaurant.app.service;

import com.restaurant.app.entity.DeliveryStatus;
import com.restaurant.app.entity.FoodOrder;
import com.restaurant.app.entity.OrderItem;
import com.restaurant.app.entity.OrderStatus;
import com.restaurant.app.entity.User;

import com.restaurant.app.repository.OrderRepository;
import com.restaurant.app.repository.UserRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public OrderService(
            OrderRepository orderRepository,
            UserRepository userRepository
    ) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    /*
     =====================================
     CREATE ORDER
     =====================================
    */

    public FoodOrder createOrder(
            FoodOrder order,
            String email
    ) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->

                        new RuntimeException(
                                "User not found"
                        )
                );

        order.setUser(user);

        order.setOrderCode(
                "ORD-"
                        + UUID.randomUUID()
                        .toString()
                        .substring(0, 8)
                        .toUpperCase()
        );

        order.setStatus(
                OrderStatus.PENDING
        );

        order.setDeliveryStatus(
                DeliveryStatus.PENDING
        );

        /*
         =====================================
         LINK ORDER ITEMS
         =====================================
        */

        double totalAmount = 0.0;

        if (order.getItems() != null) {

            for (OrderItem item : order.getItems()) {

                item.setOrder(order);

                if (
                        item.getMenuItem() != null
                                &&
                                item.getMenuItem().getPrice() != null
                ) {

                    item.setPrice(
                            item.getMenuItem().getPrice()
                    );

                    item.setTotalPrice(
                            item.getPrice()
                                    * item.getQuantity()
                    );

                    totalAmount += item.getTotalPrice();
                }
            }
        }

        order.setTotalAmount(totalAmount);

        return orderRepository.save(order);
    }

    /*
     =====================================
     GET ALL ORDERS
     =====================================
    */

    public List<FoodOrder> getAllOrders() {

        return orderRepository.findAll();
    }

    /*
     =====================================
     GET MY ORDERS
     =====================================
    */

    public List<FoodOrder> getMyOrders(
            String email
    ) {

        return orderRepository.findByUserEmail(email);
    }

    /*
     =====================================
     GET ORDER BY ID
     =====================================
    */

    public FoodOrder getOrderById(
            Long id
    ) {

        return orderRepository.findById(id)
                .orElseThrow(() ->

                        new RuntimeException(
                                "Order not found"
                        )
                );
    }

    /*
     =====================================
     UPDATE ORDER STATUS
     =====================================
    */

    public FoodOrder updateStatus(
            Long id,
            OrderStatus status
    ) {

        FoodOrder order =
                getOrderById(id);

        order.setStatus(status);

        return orderRepository.save(order);
    }

    /*
     =====================================
     UPDATE DELIVERY STATUS
     =====================================
    */

    public FoodOrder updateDeliveryStatus(
            Long id,
            DeliveryStatus status
    ) {

        FoodOrder order =
                getOrderById(id);

        order.setDeliveryStatus(status);

        return orderRepository.save(order);
    }

    /*
     =====================================
     DELETE ORDER
     =====================================
    */

    public void deleteOrder(
            Long id
    ) {

        FoodOrder order =
                getOrderById(id);

        orderRepository.delete(order);
    }
}