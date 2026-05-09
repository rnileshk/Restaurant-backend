package com.restaurant.app.repository;

import com.restaurant.app.entity.Role;
import com.restaurant.app.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository
        extends JpaRepository<User, Long> {

    Optional<User> findByEmail(
            String email
    );

    boolean existsByEmail(
            String email
    );

    List<User> findByRole(
            Role role
    );
}