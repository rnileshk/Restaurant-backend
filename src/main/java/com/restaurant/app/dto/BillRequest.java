package com.restaurant.app.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BillRequest {

    private Long orderId;

    private Double subtotal;

    private Double tax;

    private Double deliveryCharge;

    private Double totalAmount;

    private String paymentStatus;
}