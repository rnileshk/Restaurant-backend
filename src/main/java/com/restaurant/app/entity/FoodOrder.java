package com.restaurant.app.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "food_orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FoodOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderCode;

    @ManyToOne
    private User user;

    @OneToMany(
        mappedBy = "order",
        cascade = CascadeType.ALL
    )
    private List<OrderItem> items;

    private Double totalAmount;

    private String deliveryAddress;

    private String paymentMethod;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    private Double deliveryLatitude;

    private Double deliveryLongitude;

    private String deliveryAgentName;

    private String estimatedDeliveryTime;

    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {

        createdAt = LocalDateTime.now();

        if (status == null) {
            status = OrderStatus.PENDING;
        }

        if (deliveryStatus == null) {
            deliveryStatus = DeliveryStatus.PENDING;
        }
    }
}