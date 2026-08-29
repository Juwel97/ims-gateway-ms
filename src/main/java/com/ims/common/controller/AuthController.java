package com.ims.common.controller;

import com.ims.common.dto.AuthResponse;
import com.ims.common.dto.LoginRequest;
import com.ims.common.model.User;
import com.ims.common.repository.UserRepository;
import com.ims.common.service.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String accessToken = jwtService.generateAccessToken(user.getEmail(), user.getRole().name());
        String refreshToken = jwtService.generateRefreshToken(user.getEmail(), user.getRole().name());

        return ResponseEntity.ok(new AuthResponse(accessToken, refreshToken,"Bearer"));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new RuntimeException("Refresh token is required");
        }

        var claims = jwtService.parseToken(refreshToken);
        String email = claims.getSubject();
        String role = claims.get("role", String.class);

        String newAccessToken = jwtService.generateAccessToken(email, role);
        String newRefreshToken = jwtService.generateRefreshToken(email, role);

        return ResponseEntity.ok(new AuthResponse(newAccessToken, newRefreshToken, "Bearer"));
    }
}
