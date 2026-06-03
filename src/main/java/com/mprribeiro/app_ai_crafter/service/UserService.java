package com.mprribeiro.app_ai_crafter.service;

import com.mprribeiro.app_ai_crafter.dto.auth.UserProfileResponse;

public interface UserService {

    UserProfileResponse getProfile(final Long userId);
}
