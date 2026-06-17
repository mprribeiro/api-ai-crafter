package com.mprribeiro.app_ai_crafter.repository;

import com.mprribeiro.app_ai_crafter.entity.Subscription;
import com.mprribeiro.app_ai_crafter.enums.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    Optional<Subscription> findByUserIdAndStatusIn(Long userId, Set<SubscriptionStatus> active);

    Boolean existsByGatewaySubscriptionId(String subscriptionId);

    Optional<Subscription> findByGatewaySubscriptionId(String subscriptionId);
}
