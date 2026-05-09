package com.restaurant.app.dto;

import com.restaurant.app.entity.Role;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateEmployeeRequest {

    private String name;

    private String email;

    private String phone;

    private String password;

    private Role role;
}