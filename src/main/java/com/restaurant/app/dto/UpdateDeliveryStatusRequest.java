package com.restaurant.app.dto;

import com.restaurant.app.entity.DeliveryStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateDeliveryStatusRequest {

    private DeliveryStatus status;
}