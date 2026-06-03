package com.mprribeiro.app_ai_crafter.service.impl;

import com.mprribeiro.app_ai_crafter.dto.subscription.CheckoutRequest;
import com.mprribeiro.app_ai_crafter.dto.subscription.CheckoutResponse;
import com.mprribeiro.app_ai_crafter.dto.subscription.PortalResponse;
import com.mprribeiro.app_ai_crafter.dto.subscription.SubscriptionResponse;
import com.mprribeiro.app_ai_crafter.service.SubscriptionService;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    @Override
    public SubscriptionResponse getCurrentSubscription(Long userId) {
        return null;
    }

    @Override
    public CheckoutResponse createCheckoutSessionUrl(Long userId, CheckoutRequest request) {
        return null;
    }

    @Override
    public PortalResponse openCustomerPortal(long userId) {
        return null;
    }
}
