package com.mprribeiro.app_ai_crafter.dto.project;

import jakarta.validation.constraints.NotBlank;

public record ProjectRequest(@NotBlank String name) {
}
