package com.mprribeiro.app_ai_crafter.service;

import com.mprribeiro.app_ai_crafter.dto.subscription.PlanResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PlanService {

    List<PlanResponse> getActivePlans();
}
