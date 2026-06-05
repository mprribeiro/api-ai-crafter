package com.mprribeiro.app_ai_crafter.service.impl;

import com.mprribeiro.app_ai_crafter.dto.auth.AuthResponse;
import com.mprribeiro.app_ai_crafter.dto.auth.LoginRequest;
import com.mprribeiro.app_ai_crafter.dto.auth.SignupRequest;
import com.mprribeiro.app_ai_crafter.entity.User;
import com.mprribeiro.app_ai_crafter.exception.BadRequestException;
import com.mprribeiro.app_ai_crafter.mapper.UserMapper;
import com.mprribeiro.app_ai_crafter.repository.UserRepository;
import com.mprribeiro.app_ai_crafter.security.AuthUtil;
import com.mprribeiro.app_ai_crafter.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthUtil authUtil;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponse signup(final SignupRequest request) {
        userRepository.findByUsername(request.username()).ifPresent(user -> {
            throw new BadRequestException("Username: " + user.getUsername() + " already exists!");
        });

        User user = userMapper.toUserFromSignupRequest(request);
        user.setPassword(passwordEncoder.encode(request.password()));
        user = userRepository.save(user);

        String token = authUtil.generateToken(user);

        return new AuthResponse(token, userMapper.toUserProfileResponseFromUser(user));
    }

    @Override
    public AuthResponse login(final LoginRequest request) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                ));

        User user = (User) authentication.getPrincipal();
        String token = authUtil.generateToken(user);
        return new AuthResponse(token, userMapper.toUserProfileResponseFromUser(user));
    }
}
