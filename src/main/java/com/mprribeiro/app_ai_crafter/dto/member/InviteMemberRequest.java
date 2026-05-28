package com.mprribeiro.app_ai_crafter.dto.member;

import com.mprribeiro.app_ai_crafter.enums.ProjectRole;

public record InviteMemberRequest(String email, ProjectRole role) {
}
