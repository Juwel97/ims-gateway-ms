package com.ims.common.service;

import com.ims.common.dto.AuthResponse;
import com.ims.common.dto.ChangePasswordRequest;
import com.ims.common.dto.LoginRequest;
import com.ims.common.model.User;
import com.ims.common.repository.UserRepository;
import io.jsonwebtoken.Claims;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findById(request.getUserId())
            .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            user.setLoginAttempt(user.getLoginAttempt() + 1);
            userRepository.save(user);
            throw new RuntimeException("Invalid credentials");
        }

        user.setLoginAttempt(0);
        userRepository.save(user);

        String accessToken = jwtService.generateAccessToken(String.valueOf(user.getId()), user.getRole().name());
        String refreshToken = jwtService.generateRefreshToken(String.valueOf(user.getId()), user.getRole().name());

        return new AuthResponse(accessToken, refreshToken, "Bearer");
    }

    public AuthResponse refresh(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new RuntimeException("Refresh token is required");
        }

        Claims claims = jwtService.parseToken(refreshToken);
        String userId = claims.getSubject();
        String role = claims.get("role", String.class);

        String newAccessToken = jwtService.generateAccessToken(userId, role);
        String newRefreshToken = jwtService.generateRefreshToken(userId, role);

        return new AuthResponse(newAccessToken, newRefreshToken, "Bearer");
    }

    public Map<String, String> changePassword(String authHeader, ChangePasswordRequest request) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Access token is required");
        }

        String token = authHeader.substring(7).trim();
        Claims claims = jwtService.parseToken(token);
        String tokenUserId = claims.getSubject();

        if (!String.valueOf(request.getUserId()).equals(tokenUserId)) {
            throw new RuntimeException("Access token user mismatch");
        }

        return changePassword(request);
    }

    public Map<String, String> changePassword(ChangePasswordRequest request) {
        User user = userRepository.findById(request.getUserId())
            .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        return Map.of("message", "Password changed successfully");
    }
}
