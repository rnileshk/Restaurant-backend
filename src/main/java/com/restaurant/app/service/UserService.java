package com.restaurant.app.service;

import com.restaurant.app.entity.User;

import com.restaurant.app.repository.UserRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(
            UserRepository userRepository
    ) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserByEmail(
            String email
    ) {

        return userRepository
                .findByEmail(email)
                .orElseThrow();
    }

    public User saveUser(
            User user
    ) {
        return userRepository.save(user);
    }

    public void deleteUser(
            Long id
    ) {
        userRepository.deleteById(id);
    }
}