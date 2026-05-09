package com.restaurant.app.dto;

import com.restaurant.app.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResponse {

    private String token;
    private String refreshToken;
    private String name;
    private String email;
    private Role role;
}