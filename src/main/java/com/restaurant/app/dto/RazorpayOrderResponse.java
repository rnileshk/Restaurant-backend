package com.restaurant.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RazorpayOrderResponse {

    private String razorpayOrderId;

    private String currency;

    private Integer amount;

    private String key;
}