package com.mprribeiro.app_ai_crafter.service;

import com.mprribeiro.app_ai_crafter.dto.subscription.CheckoutRequest;
import com.mprribeiro.app_ai_crafter.dto.subscription.CheckoutResponse;
import com.mprribeiro.app_ai_crafter.dto.subscription.PortalResponse;
import com.mprribeiro.app_ai_crafter.dto.subscription.SubscriptionResponse;

public interface SubscriptionService {

    SubscriptionResponse getCurrentSubscription(final Long userId);

    CheckoutResponse createCheckoutSessionUrl(final Long userId, final CheckoutRequest request);

    PortalResponse openCustomerPortal(long userId);
}
