package com.mprribeiro.app_ai_crafter.mapper;

import com.mprribeiro.app_ai_crafter.dto.subscription.PlanResponse;
import com.mprribeiro.app_ai_crafter.dto.subscription.SubscriptionResponse;
import com.mprribeiro.app_ai_crafter.entity.Plan;
import com.mprribeiro.app_ai_crafter.entity.Subscription;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {

    SubscriptionResponse toSubscriptionResponse(Subscription subscription);

    PlanResponse toPlanResponse(Plan plan);
}
