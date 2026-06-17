package com.mprribeiro.app_ai_crafter.service.impl;

import com.mprribeiro.app_ai_crafter.dto.subscription.PlanResponse;
import com.mprribeiro.app_ai_crafter.mapper.PlanMapper;
import com.mprribeiro.app_ai_crafter.repository.PlanRepository;
import com.mprribeiro.app_ai_crafter.service.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanServiceImpl implements PlanService {

    private final PlanRepository planRepository;
    private final PlanMapper planMapper;

    @Override
    public List<PlanResponse> getActivePlans() {
        final var plans = planRepository.findAllByActiveTrue();
        return plans.stream().map(planMapper::toPlanResponseFromPlan).toList();
    }
}
