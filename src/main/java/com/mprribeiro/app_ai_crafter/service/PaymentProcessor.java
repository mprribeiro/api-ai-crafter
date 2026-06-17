package com.mprribeiro.app_ai_crafter.service;

import com.mprribeiro.app_ai_crafter.dto.subscription.CheckoutRequest;
import com.mprribeiro.app_ai_crafter.dto.subscription.CheckoutResponse;
import com.mprribeiro.app_ai_crafter.dto.subscription.PortalResponse;
import com.stripe.model.StripeObject;

import java.util.Map;

public interface PaymentProcessor {

    CheckoutResponse createCheckoutSessionUrl(final CheckoutRequest request);

    PortalResponse openCustomerPortal();

    void handleWebhookEvent(final String type,
                            final StripeObject stripeObject,
                            final Map<String, String> metadata);
}
