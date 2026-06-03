package com.mprribeiro.app_ai_crafter.service;

import com.mprribeiro.app_ai_crafter.dto.subscription.PlanResponse;

import java.util.List;

public interface PlanService {

    List<PlanResponse> getActivePlans();
}
