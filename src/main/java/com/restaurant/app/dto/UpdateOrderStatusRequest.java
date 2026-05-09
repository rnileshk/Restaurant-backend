package com.restaurant.app.dto;

import com.restaurant.app.entity.OrderStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateOrderStatusRequest {

    private OrderStatus status;
}