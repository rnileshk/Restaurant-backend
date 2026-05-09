package com.restaurant.app.controller;

import com.restaurant.app.entity.User;
import com.restaurant.app.repository.UserRepository;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@CrossOrigin("*")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/profile")
    public User getProfile(Authentication authentication) {

        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow();
    }

    @PutMapping("/profile")
    public User updateProfile(
            Authentication authentication,
            @RequestBody User updatedUser
    ) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow();

        user.setName(updatedUser.getName());
        user.setPhone(updatedUser.getPhone());

        return userRepository.save(user);
    }
}