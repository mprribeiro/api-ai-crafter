package com.mprribeiro.app_ai_crafter.service.impl;

import com.mprribeiro.app_ai_crafter.dto.auth.UserProfileResponse;
import com.mprribeiro.app_ai_crafter.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public UserProfileResponse getProfile(Long userId) {
        return null;
    }
}
