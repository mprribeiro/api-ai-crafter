package com.mprribeiro.app_ai_crafter.service;

import com.mprribeiro.app_ai_crafter.dto.auth.UserProfileResponse;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    UserProfileResponse getProfile(final Long userId);
}
