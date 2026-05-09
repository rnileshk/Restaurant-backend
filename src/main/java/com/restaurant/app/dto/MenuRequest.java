package com.restaurant.app.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuRequest {

    private String name;

    private String description;

    private Double price;

    private String image;

    private Boolean available;

    private String category;
}