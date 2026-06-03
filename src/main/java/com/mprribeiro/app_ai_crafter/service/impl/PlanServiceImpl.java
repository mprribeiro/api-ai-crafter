package com.mprribeiro.app_ai_crafter.service.impl;

import com.mprribeiro.app_ai_crafter.dto.subscription.PlanResponse;
import com.mprribeiro.app_ai_crafter.service.PlanService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanServiceImpl implements PlanService {

    @Override
    public List<PlanResponse> getActivePlans() {
        return List.of();
    }
}
