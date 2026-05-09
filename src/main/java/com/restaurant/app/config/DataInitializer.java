package com.restaurant.app.config;

import com.restaurant.app.entity.Role;
import com.restaurant.app.entity.User;
import com.restaurant.app.repository.UserRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        String adminEmail = "admin@restaurant.com";

        if (!userRepository.existsByEmail(adminEmail)) {

            User admin = User.builder()
                    .name("Restaurant Admin")
                    .email(adminEmail)
                    .phone("9999999999")
                    .password(passwordEncoder.encode("admin123"))
                    .role(Role.ADMIN)
                    .active(true)
                    .build();

            userRepository.save(admin);

            System.out.println("Default admin created:");
            System.out.println("Email: admin@restaurant.com");
            System.out.println("Password: admin123");
        }
    }
}