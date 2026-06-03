package com.mprribeiro.app_ai_crafter.mapper;

import com.mprribeiro.app_ai_crafter.dto.auth.SignupRequest;
import com.mprribeiro.app_ai_crafter.dto.auth.UserProfileResponse;
import com.mprribeiro.app_ai_crafter.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUserFromSignupRequest(SignupRequest request);

    UserProfileResponse toUserProfileResponseFromUser(User user);
}
