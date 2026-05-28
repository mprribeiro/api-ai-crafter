package com.mprribeiro.app_ai_crafter.controller;

import com.mprribeiro.app_ai_crafter.dto.subscription.PlanLimitsResponse;
import com.mprribeiro.app_ai_crafter.dto.subscription.UsageTodayResponse;
import com.mprribeiro.app_ai_crafter.service.UsageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/usage")
public class UsageController {

    private final UsageService usageService;

    @GetMapping("/today")
    public ResponseEntity<UsageTodayResponse> getTodayUsage() {
        final var userId = 1L;
        return ResponseEntity.ok(usageService.getTodayUsage(userId));
    }

    @GetMapping("/limits")
    public ResponseEntity<PlanLimitsResponse> getPlanLimits() {
        final var userId = 1L;
        return ResponseEntity.ok(usageService.getCurrentPlanLimits(userId));
    }


}
