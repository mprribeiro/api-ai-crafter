package com.mprribeiro.app_ai_crafter.entity;

import com.mprribeiro.app_ai_crafter.enums.SubscriptionStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Subscription {

    Long id;

    User user;

    Plan plan;

    @Enumerated(value = EnumType.STRING)
    SubscriptionStatus status;

    String stripeSubscriptionId; //can be renamed to gatewaySubscriptionId

    Instant currentPeriodStart;
    Instant currentPeriodEnd;
    Boolean cancelAtPeriodEnd = false;

    Instant createdAt;
    Instant updatedAt;
}
