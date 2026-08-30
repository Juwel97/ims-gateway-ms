package com.ims.common.controller;

import com.ims.common.dto.AuthResponse;
import com.ims.common.dto.ChangePasswordRequest;
import com.ims.common.dto.LoginRequest;
import com.ims.common.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody Map<String, String> request) {
        return ResponseEntity.ok(authService.refresh(request.get("refreshToken")));
    }

    @PostMapping("/change-password")
    public ResponseEntity<Map<String, String>> changePassword(
        @RequestHeader(value = "Authorization", required = false) String authHeader,
        @Valid @RequestBody ChangePasswordRequest request) {

        return ResponseEntity.ok(authService.changePassword(authHeader, request));
    }
}
