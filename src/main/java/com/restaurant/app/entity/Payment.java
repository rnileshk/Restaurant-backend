package com.restaurant.app.entity;

import jakarta.persistence.*;

import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String transactionId;

    @OneToOne
    private FoodOrder order;

    private Double amount;

    private String paymentMethod;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    private LocalDateTime paymentDate;

    @PrePersist
    public void onCreate() {

        paymentDate = LocalDateTime.now();

        if (paymentStatus == null) {
            paymentStatus = PaymentStatus.PENDING;
        }
    }
}