package com.mprribeiro.app_ai_crafter.service.impl;

import com.mprribeiro.app_ai_crafter.dto.subscription.PlanLimitsResponse;
import com.mprribeiro.app_ai_crafter.dto.subscription.UsageTodayResponse;
import com.mprribeiro.app_ai_crafter.service.UsageService;
import org.springframework.stereotype.Service;

@Service
public class UsageServiceImpl implements UsageService {

    @Override
    public UsageTodayResponse getTodayUsage(long userId) {
        return null;
    }

    @Override
    public PlanLimitsResponse getCurrentPlanLimits(long userId) {
        return null;
    }
}
