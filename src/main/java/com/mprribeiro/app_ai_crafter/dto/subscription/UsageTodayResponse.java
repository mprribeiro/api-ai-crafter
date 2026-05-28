package com.mprribeiro.app_ai_crafter.dto.subscription;

public record UsageTodayResponse(Integer tokensUsed,
                                 Integer tokenLimit,
                                 Integer previewRunning,
                                 Integer previewsLimit) {
}
