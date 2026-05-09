package com.restaurant.app.service;

import com.restaurant.app.dto.AuthRequest;
import com.restaurant.app.dto.AuthResponse;
import com.restaurant.app.dto.RegisterRequest;

import com.restaurant.app.entity.RefreshToken;
import com.restaurant.app.entity.Role;
import com.restaurant.app.entity.User;

import com.restaurant.app.repository.UserRepository;
import com.restaurant.app.security.JwtService;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;

    public AuthService(
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

    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .active(true)
                .build();

        User savedUser = userRepository.save(user);

        String token = jwtService.generateToken(savedUser);

        RefreshToken refreshToken =
                refreshTokenService.createRefreshToken(savedUser.getEmail());

        return new AuthResponse(
                token,
                refreshToken.getToken(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getRole()
        );
    }

    public AuthResponse login(AuthRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtService.generateToken(user);

        RefreshToken refreshToken =
                refreshTokenService.createRefreshToken(user.getEmail());

        return new AuthResponse(
                token,
                refreshToken.getToken(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }

    public AuthResponse refreshAccessToken(String refreshTokenValue) {

        RefreshToken refreshToken =
                refreshTokenService.findByToken(refreshTokenValue);

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
}