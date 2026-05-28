package com.mprribeiro.app_ai_crafter.service;

import com.mprribeiro.app_ai_crafter.dto.subscription.PlanLimitsResponse;
import com.mprribeiro.app_ai_crafter.dto.subscription.UsageTodayResponse;
import org.springframework.stereotype.Service;

@Service
public interface UsageService {
    UsageTodayResponse getTodayUsage(long userId);

    PlanLimitsResponse getCurrentPlanLimits(long userId);
}
