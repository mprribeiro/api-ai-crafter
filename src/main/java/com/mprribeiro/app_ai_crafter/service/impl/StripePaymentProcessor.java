package com.mprribeiro.app_ai_crafter.service.impl;

import com.mprribeiro.app_ai_crafter.dto.subscription.CheckoutRequest;
import com.mprribeiro.app_ai_crafter.dto.subscription.CheckoutResponse;
import com.mprribeiro.app_ai_crafter.dto.subscription.PortalResponse;
import com.mprribeiro.app_ai_crafter.entity.Plan;
import com.mprribeiro.app_ai_crafter.enums.SubscriptionStatus;
import com.mprribeiro.app_ai_crafter.exception.BadRequestException;
import com.mprribeiro.app_ai_crafter.exception.PaymentSessionCreationException;
import com.mprribeiro.app_ai_crafter.exception.PortalSessionCreationException;
import com.mprribeiro.app_ai_crafter.exception.ResourceNotFoundException;
import com.mprribeiro.app_ai_crafter.repository.PlanRepository;
import com.mprribeiro.app_ai_crafter.repository.UserRepository;
import com.mprribeiro.app_ai_crafter.security.AuthUtil;
import com.mprribeiro.app_ai_crafter.service.PaymentProcessor;
import com.mprribeiro.app_ai_crafter.service.SubscriptionService;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;

import static java.util.Objects.isNull;

@Slf4j
@Service
@RequiredArgsConstructor
public class StripePaymentProcessor implements PaymentProcessor {

    @Value("${client.url}")
    private String frontendUrl;

    private final AuthUtil authUtil;
    private final PlanRepository planRepository;
    private final UserRepository userRepository;
    private final SubscriptionService subscriptionService;

    @Override
    public CheckoutResponse createCheckoutSessionUrl(final CheckoutRequest request) {
        final var userId = authUtil.getCurrentUserId();
        final var plan = planRepository.findById(request.planId()).orElseThrow(
                () -> new ResourceNotFoundException("Plan not found for id " + request.planId()));
        final var user = userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException("User not found for id " + userId));

        var params = SessionCreateParams.builder()
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setPrice(plan.getStripePriceId())
                                .setQuantity(1L)
                                .build())
                .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                .setSubscriptionData(
                        new SessionCreateParams.SubscriptionData.Builder()
                                .setBillingMode(
                                        SessionCreateParams.SubscriptionData.BillingMode.builder()
                                                .setType(SessionCreateParams.SubscriptionData.BillingMode.Type.FLEXIBLE)
                                                .build())
                                .build()
                )
                .setSuccessUrl(frontendUrl + "/success.html?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl(frontendUrl + "/cancel.html")
                .putMetadata("user_id", userId.toString())
                .putMetadata("plan_id", plan.getId().toString());
        try {
            String stripeCustomerId = user.getStripeCustomerId();

            if (isNull(stripeCustomerId) || stripeCustomerId.isEmpty()) {
                params.setCustomerEmail(user.getUsername());
            } else {
                params.setCustomer(stripeCustomerId);
            }

            Session session = Session.create(params.build());
            return new CheckoutResponse(session.getUrl());
        } catch (StripeException e) {
            throw new PaymentSessionCreationException(e.getMessage());
        }
    }

    @Override
    public PortalResponse openCustomerPortal() {
        final var userId = authUtil.getCurrentUserId();
        final var user = userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException("User not found for id " + userId));

        final var stripeCustomerId = user.getStripeCustomerId();

        if (isNull(stripeCustomerId) || stripeCustomerId.isEmpty()) {
            throw new BadRequestException("User does not have a Stripe Customer Id. UserId: " + userId);
        }

        com.stripe.model.billingportal.Session portalSession = null;
        try {
            portalSession = com.stripe.model.billingportal.Session.create(
                    com.stripe.param.billingportal.SessionCreateParams.builder()
                            .setCustomer(stripeCustomerId)
                            .setReturnUrl(frontendUrl)
                            .build()
            );
        } catch (StripeException e) {
            throw new PortalSessionCreationException("Unable to create Portal for user " + userId);
        }

        return new PortalResponse(portalSession.getUrl());
    }

    @Override
    public void handleWebhookEvent(final String type,
                                   final StripeObject stripeObject,
                                   final Map<String, String> metadata) {
        log.info("Handling event {}", type);

        switch (type) {
            case "checkout.session.completed" -> handleCheckoutSessionCompleted((Session) stripeObject, metadata); //one-time on checkout completed
            case "customer.subscription.updated" -> handleCustomerSubscriptionUpdated((Subscription) stripeObject); // when user cancels, upgrades or any other update
            case "customer.subscription.deleted" -> handleCustomerSubscriptionDeleted((Subscription) stripeObject); // when subscription ends
            case "invoice.paid" -> handleInvoicePaid((Invoice) stripeObject); // when invoice is paid
            case "invoice.payment_failed" -> handleInvoicePaymentFailed((Invoice) stripeObject); // invoice marked as PAST_DUE
            default -> log.debug("Ignoring the event {}", type);
        }
    }

    private void handleInvoicePaymentFailed(final Invoice invoice) {

        if (isNull(invoice)) {
            log.error("handleInvoicePaymentFailed - Invoice is null.");
            return;
        }

        final var subscriptionId = extractSubscriptionId(invoice);
        if (isNull(subscriptionId)) return;

        subscriptionService.markSubscriptionPastDue(subscriptionId);
    }

    private void handleInvoicePaid(final Invoice invoice) {

        if (isNull(invoice)) {
            log.error("handleInvoicePaid - Invoice is null.");
            return;
        }

        final var subscriptionId = extractSubscriptionId(invoice);
        if (isNull(subscriptionId)) return;

        try {
            Subscription subscription = Subscription.retrieve(subscriptionId);
            var item = subscription.getItems().getData().getFirst();

            final var periodStart = toInstant(item.getCurrentPeriodStart());
            final var periodEnd = toInstant(item.getCurrentPeriodEnd());

            subscriptionService.renewSubscriptionPeriod(subscriptionId, periodStart, periodEnd);
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleCustomerSubscriptionDeleted(final Subscription subscription) {

        if (isNull(subscription)) {
            log.error("handleCustomerSubscriptionDeleted - Subscription is null.");
            return;
        }

        subscriptionService.cancelSubscription(subscription.getId());
    }

    private void handleCustomerSubscriptionUpdated(final Subscription subscription) {

        if (isNull(subscription)) {
            log.error("handleCustomerSubscriptionUpdated - Subscription is null.");
            return;
        }

        SubscriptionStatus status = mapStripeStatusEnum(subscription.getStatus());
        if (isNull(status)) {
            log.warn("Unknown status '{}' for subscription {}", subscription.getStatus(), subscription.getId());
            return;
        }

        SubscriptionItem item = subscription.getItems().getData().getFirst();
        Instant periodStart = toInstant(item.getCurrentPeriodStart());
        Instant periodEnd = toInstant(item.getCurrentPeriodEnd());

        Long planId = resolvePlanId(item.getPrice());

        subscriptionService.updateSubscription(subscription.getId(), status, periodStart, periodEnd,
                subscription.getCancelAtPeriodEnd(), planId);
    }

    private Long resolvePlanId(final Price price) {
        return planRepository.findByStripePriceId(price.getId())
                .map(Plan::getId)
                .orElseThrow(() -> new ResourceNotFoundException("Plan not found for price id " + price.getId()));
    }

    private Instant toInstant(final Long epoch) {
        return Objects.nonNull(epoch) ? Instant.ofEpochSecond(epoch) : null;
    }

    private SubscriptionStatus mapStripeStatusEnum(final String status) {
        return switch (status) {
            case "active" -> SubscriptionStatus.ACTIVE;
            case "trialing" -> SubscriptionStatus.TRIALING;
            case "past_due", "unpaid", "paused", "incomplete_expired" -> SubscriptionStatus.PAST_DUE;
            case "canceled" -> SubscriptionStatus.CANCELED;
            case "incomplete" -> SubscriptionStatus.INCOMPLETE;
            default -> {
                log.warn("Unmapped Stripe status: {}", status);
                yield null;
            }
        };
    }

    private void handleCheckoutSessionCompleted(final Session session, final Map<String, String> metadata) {

        if (isNull(session)) {
            log.error("handleCheckoutSessionCompleted - Session is null.");
            return;
        }

        final var userId = Long.parseLong(metadata.get("user_id"));
        final var planId = Long.parseLong(metadata.get("plan_id"));

        final var user = userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException("User not found for id " + userId));

        final var customerId = session.getCustomer();
        final var subscriptionId = session.getSubscription();

        if (isNull(user.getStripeCustomerId())) {
            user.setStripeCustomerId(customerId);
            userRepository.save(user);
        }

        subscriptionService.activateSubscripton(userId, planId, subscriptionId, customerId);
    }

    private String extractSubscriptionId(final Invoice invoice) {
        var parent = invoice.getParent();
        if (isNull(parent)) return null;

        var subDetails = parent.getSubscriptionDetails();
        if (isNull(subDetails)) return null;

        return subDetails.getSubscription();
    }
}
