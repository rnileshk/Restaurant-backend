package com.restaurant.app.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderRequest {

    private Long userId;

    private List<OrderItemRequest> items;

    private String deliveryAddress;

    private String paymentMethod;

    private Double totalAmount;
}