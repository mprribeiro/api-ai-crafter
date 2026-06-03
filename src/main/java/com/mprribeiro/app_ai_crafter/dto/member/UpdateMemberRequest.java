package com.mprribeiro.app_ai_crafter.dto.member;

import com.mprribeiro.app_ai_crafter.enums.ProjectRole;
import jakarta.validation.constraints.NotNull;

public record UpdateMemberRequest(@NotNull ProjectRole role) {
}
