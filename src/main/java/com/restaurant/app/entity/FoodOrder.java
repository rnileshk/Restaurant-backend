package com.restaurant.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
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

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "password", "authorities"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<OrderItem> items = new ArrayList<>();

    @JsonIgnore
    @OneToOne(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Payment payment;

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
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }

        if (orderCode == null || orderCode.isBlank()) {
            orderCode = "ORD-" + System.currentTimeMillis();
        }

        if (status == null) {
            status = OrderStatus.PENDING;
        }

        if (deliveryStatus == null) {
            deliveryStatus = DeliveryStatus.PENDING;
        }
    }
}