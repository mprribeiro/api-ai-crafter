package com.mprribeiro.app_ai_crafter.service.impl;

import com.mprribeiro.app_ai_crafter.dto.subscription.SubscriptionResponse;
import com.mprribeiro.app_ai_crafter.entity.Subscription;
import com.mprribeiro.app_ai_crafter.enums.SubscriptionStatus;
import com.mprribeiro.app_ai_crafter.exception.ResourceNotFoundException;
import com.mprribeiro.app_ai_crafter.mapper.SubscriptionMapper;
import com.mprribeiro.app_ai_crafter.repository.PlanRepository;
import com.mprribeiro.app_ai_crafter.repository.ProjectMemberRepository;
import com.mprribeiro.app_ai_crafter.repository.SubscriptionRepository;
import com.mprribeiro.app_ai_crafter.repository.UserRepository;
import com.mprribeiro.app_ai_crafter.security.AuthUtil;
import com.mprribeiro.app_ai_crafter.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private static final Integer FREE_TIER_PROJECTS_ALLOWED = 1;

    private final AuthUtil authUtil;
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionMapper subscriptionMapper;
    private final PlanRepository planRepository;
    private final UserRepository userRepository;
    private final ProjectMemberRepository projectMemberRepository;

    @Override
    public SubscriptionResponse getCurrentSubscription() {
        final var userId = authUtil.getCurrentUserId();
        final var sub = subscriptionRepository.findByUserIdAndStatusIn(userId, Set.of(
                SubscriptionStatus.ACTIVE, SubscriptionStatus.PAST_DUE,
                SubscriptionStatus.TRIALING
        )).orElseThrow(() -> new ResourceNotFoundException("Active subscription not found for user " + userId));

        return subscriptionMapper.toSubscriptionResponse(sub);
    }

    @Override
    public void activateSubscripton(final Long userId,
                                    final Long planId,
                                    final String subscriptionId,
                                    final String customerId) {

        final var exists = subscriptionRepository.existsByGatewaySubscriptionId(subscriptionId);
        if (exists) return;

        final var user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        final var plan = planRepository.findById(planId)
                .orElseThrow(() -> new ResourceNotFoundException("Plan not found with id: " + planId));

        final var sub = Subscription.builder()
                .user(user)
                .plan(plan)
                .status(SubscriptionStatus.INCOMPLETE)
                .gatewaySubscriptionId(subscriptionId)
                .cancelAtPeriodEnd(false)
                .build();

        subscriptionRepository.save(sub);

    }

    @Override
    public void updateSubscription(final String subscriptionId,
                                   final SubscriptionStatus status,
                                   final Instant periodStart,
                                   final Instant periodEnd,
                                   final Boolean cancelAtPeriodEnd,
                                   final Long planId) {
        Subscription subscription = subscriptionRepository.findByGatewaySubscriptionId(subscriptionId)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription not found for subscriptionId " + subscriptionId));

        subscription.setStatus(status);
        subscription.setCurrentPeriodStart(periodStart);
        subscription.setCurrentPeriodEnd(periodEnd);
        subscription.setCancelAtPeriodEnd(cancelAtPeriodEnd);

        if (Objects.nonNull(planId) && !subscription.getPlan().getId().equals(planId)) {
            final var plan = planRepository.findById(planId)
                    .orElseThrow(() -> new ResourceNotFoundException("Plan not found with id: " + planId));
            subscription.setPlan(plan);
        }

        subscriptionRepository.save(subscription);
    }

    @Override
    public void cancelSubscription(final String subscriptionId) {
        Subscription subscription = subscriptionRepository.findByGatewaySubscriptionId(subscriptionId)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription not found for subscriptionId " + subscriptionId));

        subscription.setStatus(SubscriptionStatus.CANCELED);
        subscriptionRepository.save(subscription);
    }

    @Override
    public void renewSubscriptionPeriod(final String subscriptionId,
                                        final Instant periodStart,
                                        final Instant periodEnd) {
        Subscription subscription = subscriptionRepository.findByGatewaySubscriptionId(subscriptionId)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription not found for subscriptionId " + subscriptionId));

        Instant newStart = Objects.nonNull(periodStart) ? periodStart : subscription.getCurrentPeriodEnd();

        subscription.setCurrentPeriodStart(newStart);
        subscription.setCurrentPeriodEnd(periodEnd);

        if(subscription.getStatus() == SubscriptionStatus.PAST_DUE ||
                subscription.getStatus() == SubscriptionStatus.INCOMPLETE) {
            subscription.setStatus(SubscriptionStatus.ACTIVE);
        }

        subscriptionRepository.save(subscription);
    }

    @Override
    public void markSubscriptionPastDue(final String subscriptionId) {
        Subscription subscription = subscriptionRepository.findByGatewaySubscriptionId(subscriptionId)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription not found for subscriptionId " + subscriptionId));

        if (subscription.getStatus().equals(SubscriptionStatus.PAST_DUE)) {
            log.debug("Subscription is already past due {}", subscriptionId);
        }


        subscription.setStatus(SubscriptionStatus.PAST_DUE);

        subscriptionRepository.save(subscription);

        // notify user via email..
    }

    @Override
    public Boolean canCreateNewProject() {
        Long userId = authUtil.getCurrentUserId();
        SubscriptionResponse currentSubscription = getCurrentSubscription();

        int countOfOwnedProjects = projectMemberRepository.countProjectOwnedByUser(userId);

        if(Objects.isNull(currentSubscription.plan())) {
            return countOfOwnedProjects < FREE_TIER_PROJECTS_ALLOWED;
        }

        return countOfOwnedProjects < currentSubscription.plan().maxProjects();
    }

}
