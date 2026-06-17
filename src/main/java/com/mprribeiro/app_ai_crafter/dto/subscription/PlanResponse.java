package com.mprribeiro.app_ai_crafter.dto.subscription;

public record PlanResponse(Long id,
                           String name,
                           Integer maxProjects,
                           Integer maxTokensPerDay,
                           Boolean unlimitedAi,
                           String price) {
}
