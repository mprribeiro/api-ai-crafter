package com.mprribeiro.app_ai_crafter.controller;

import com.mprribeiro.app_ai_crafter.dto.auth.AuthResponse;
import com.mprribeiro.app_ai_crafter.dto.auth.LoginRequest;
import com.mprribeiro.app_ai_crafter.dto.auth.SignupRequest;
import com.mprribeiro.app_ai_crafter.dto.auth.UserProfileResponse;
import com.mprribeiro.app_ai_crafter.service.AuthService;
import com.mprribeiro.app_ai_crafter.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@RequestBody @Valid final SignupRequest request) {
        return ResponseEntity.ok(authService.signup(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login (@RequestBody @Valid final LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getProfile() {
        final var userId = 1L;
        return ResponseEntity.ok(userService.getProfile(userId));
    }
}
