package com.mprribeiro.app_ai_crafter.controller;

import com.mprribeiro.app_ai_crafter.dto.subscription.*;
import com.mprribeiro.app_ai_crafter.service.PaymentProcessor;
import com.mprribeiro.app_ai_crafter.service.PlanService;
import com.mprribeiro.app_ai_crafter.service.SubscriptionService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequiredArgsConstructor
@Slf4j
public class BillingController {

    @Value("${stripe.webhook.secret}")
    private String webhookSecret;

    private final PlanService planService;
    private final SubscriptionService subscriptionService;
    private final PaymentProcessor paymentProcessor;

    @GetMapping("/api/plans")
    public ResponseEntity<List<PlanResponse>> getActivePlans() {
        return ResponseEntity.ok(planService.getActivePlans());
    }

    @GetMapping("/api/me/subscription")
    public ResponseEntity<SubscriptionResponse> getCurrentSubscription() {
        return ResponseEntity.ok(subscriptionService.getCurrentSubscription());
    }

    @PostMapping("/api/payments/checkout")
    public ResponseEntity<CheckoutResponse> createCheckoutResponse(@RequestBody @Valid CheckoutRequest request) {
        return ResponseEntity.ok(paymentProcessor.createCheckoutSessionUrl(request));
    }

    @PostMapping("/api/payments/portal")
    public ResponseEntity<PortalResponse> openCustomerPortal() {
        return ResponseEntity.ok(paymentProcessor.openCustomerPortal());
    }

    @PostMapping("/webhooks/payments")
    public ResponseEntity<String> handlePaymentWebhook(
            @RequestBody String paylod,
            @RequestHeader("Stripe-signature") String sigHeader) throws SignatureVerificationException {
        Event event = Webhook.constructEvent(paylod, sigHeader, webhookSecret);
        StripeObject stripeObject = getStripeObject(event);

        final ExecutorService executor = Executors.newFixedThreadPool(10);
        executor.submit(() -> {
            Map<String, String> metadata = new HashMap<>();
            if (stripeObject instanceof Session session) {
                metadata = session.getMetadata();
            }
            paymentProcessor.handleWebhookEvent(event.getType(), stripeObject, metadata);
        });

        return ResponseEntity.ok().build();
    }

    private static StripeObject getStripeObject(Event event) {
        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        StripeObject stripeObject = null;
        if (dataObjectDeserializer.getObject().isPresent()) {
            stripeObject = dataObjectDeserializer.getObject().get();
        }
        return stripeObject;
    }
}
