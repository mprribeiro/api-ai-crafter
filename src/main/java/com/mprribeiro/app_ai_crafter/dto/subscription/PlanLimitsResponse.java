package com.mprribeiro.app_ai_crafter.dto.subscription;

public record PlanLimitsResponse(String planName,
                                 Integer maxTokenPerDay,
                                 Integer maxProjects,
                                 Boolean unlimitedAi) {
}
