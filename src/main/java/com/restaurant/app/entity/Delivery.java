package com.restaurant.app.entity;

import jakarta.persistence.*;

import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "deliveries")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "order_id")
    private FoodOrder order;

    @ManyToOne
    @JoinColumn(name = "delivery_boy_id")
    private User deliveryBoy;

    private String deliveryAddress;

    private Double latitude;

    private Double longitude;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;

    private String estimatedDeliveryTime;

    private LocalDateTime assignedAt;

    private LocalDateTime deliveredAt;

    @PrePersist
    public void onCreate() {

        assignedAt = LocalDateTime.now();

        if (status == null) {
            status = DeliveryStatus.PENDING;
        }
    }
}