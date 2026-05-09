package com.restaurant.app.entity;

import jakarta.persistence.*;

import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "bills")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     =====================================
     ORDER LINK
     =====================================
    */

    @OneToOne
    @JoinColumn(name = "order_id")
    private FoodOrder order;

    /*
     =====================================
     BOOKING LINK
     =====================================
    */

    @OneToOne
    @JoinColumn(name = "booking_id")
    private TableBooking booking;

    private Double subtotal;

    private Double tax;

    private Double deliveryCharge;

    private Double totalAmount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    private LocalDateTime generatedAt;

    @PrePersist
    public void onCreate() {

        generatedAt = LocalDateTime.now();

        if (paymentStatus == null) {
            paymentStatus = PaymentStatus.PENDING;
        }
    }
}