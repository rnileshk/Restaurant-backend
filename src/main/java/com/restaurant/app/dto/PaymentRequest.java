package com.restaurant.app.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequest {

    private Long orderId;

    private Double amount;

    private String paymentMethod;

    private String transactionId;

    private String paymentStatus;
}