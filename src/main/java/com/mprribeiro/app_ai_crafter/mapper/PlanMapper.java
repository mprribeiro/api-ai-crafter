package com.mprribeiro.app_ai_crafter.mapper;

import com.mprribeiro.app_ai_crafter.dto.subscription.PlanResponse;
import com.mprribeiro.app_ai_crafter.entity.Plan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PlanMapper {

    @Mapping(target = "price", source = "stripePriceId")
    PlanResponse toPlanResponseFromPlan(Plan plan);
}
