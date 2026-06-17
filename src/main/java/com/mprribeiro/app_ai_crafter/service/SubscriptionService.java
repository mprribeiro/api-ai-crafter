package com.mprribeiro.app_ai_crafter.service;

import com.mprribeiro.app_ai_crafter.dto.subscription.SubscriptionResponse;
import com.mprribeiro.app_ai_crafter.enums.SubscriptionStatus;

import java.time.Instant;

public interface SubscriptionService {

    SubscriptionResponse getCurrentSubscription();

    void activateSubscripton(final Long userId,
                             final Long planId,
                             final String subscriptionId,
                             final String customerId);

    void updateSubscription(final String subscriptionId,
                            final SubscriptionStatus status,
                            final Instant periodStart,
                            final Instant periodEnd,
                            final Boolean cancelAtPeriodEnd,
                            final Long planId);

    void cancelSubscription(final String id);

    void renewSubscriptionPeriod(final String subscriptionId,
                                 final Instant periodStart,
                                 final Instant periodEnd);

    void markSubscriptionPastDue(final String subscriptionId);

    Boolean canCreateNewProject();
}
