package com.restaurant.app.dto;

import lombok.Data;

@Data
public class RazorpayOrderRequest {

    private Long orderId;

    private Double amount;
}