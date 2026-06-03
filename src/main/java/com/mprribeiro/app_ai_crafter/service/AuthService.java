package com.mprribeiro.app_ai_crafter.service;

import com.mprribeiro.app_ai_crafter.dto.auth.AuthResponse;
import com.mprribeiro.app_ai_crafter.dto.auth.LoginRequest;
import com.mprribeiro.app_ai_crafter.dto.auth.SignupRequest;

public interface AuthService {
    AuthResponse signup(final SignupRequest request);

    AuthResponse login(final LoginRequest request);
}
