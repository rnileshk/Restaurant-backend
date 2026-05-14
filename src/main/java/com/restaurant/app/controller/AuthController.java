package com.restaurant.app.controller;

import com.restaurant.app.dto.AuthRequest;
import com.restaurant.app.dto.AuthResponse;
import com.restaurant.app.dto.CreateEmployeeRequest;
import com.restaurant.app.dto.RefreshTokenRequest;
import com.restaurant.app.dto.RegisterRequest;
import com.restaurant.app.entity.RefreshToken;
import com.restaurant.app.entity.Role;
import com.restaurant.app.entity.User;
import com.restaurant.app.repository.UserRepository;
import com.restaurant.app.security.JwtService;
import com.restaurant.app.service.RefreshTokenService;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;

    public AuthController(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            AuthenticationManager authenticationManager,
            RefreshTokenService refreshTokenService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/register")
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        Role role = resolveRole(request.getRole());

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .active(true)
                .build();

        userRepository.save(user);

        String token = jwtService.generateToken(user);

        String refreshToken = refreshTokenService
                .createRefreshToken(user.getEmail())
                .getToken();

        return new AuthResponse(
                token,
                refreshToken,
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody AuthRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtService.generateToken(user);

        String refreshToken = refreshTokenService
                .createRefreshToken(user.getEmail())
                .getToken();

        return new AuthResponse(
                token,
                refreshToken,
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }

    @PostMapping("/create-employee")
    public AuthResponse createEmployee(@RequestBody CreateEmployeeRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        if (request.getRole() == null || request.getRole() == Role.USER) {
            throw new RuntimeException("Admin can only create employee/admin staff roles");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .active(true)
                .build();

        userRepository.save(user);

        String token = jwtService.generateToken(user);

        String refreshToken = refreshTokenService
                .createRefreshToken(user.getEmail())
                .getToken();

        return new AuthResponse(
                token,
                refreshToken,
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }

    @PostMapping("/refresh-token")
    public AuthResponse refreshToken(@RequestBody RefreshTokenRequest request) {

        RefreshToken refreshToken = refreshTokenService
                .findByToken(request.getRefreshToken());

        refreshTokenService.verifyExpiration(refreshToken);

        User user = refreshToken.getUser();

        String newAccessToken = jwtService.generateToken(user);

        return new AuthResponse(
                newAccessToken,
                refreshToken.getToken(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }

    private Role resolveRole(String requestRole) {
        if (requestRole == null || requestRole.isBlank()) {
            return Role.USER;
        }

        String cleanedRole = requestRole
                .trim()
                .replace("ROLE_", "")
                .toUpperCase();

        try {
            return Role.valueOf(cleanedRole);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid role: " + requestRole);
        }
    }
}