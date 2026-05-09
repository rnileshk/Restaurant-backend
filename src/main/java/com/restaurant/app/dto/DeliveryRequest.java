package com.restaurant.app.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeliveryRequest {

    private Long orderId;

    private Long deliveryBoyId;

    private String deliveryAddress;

    private Double latitude;

    private Double longitude;
}