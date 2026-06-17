package com.mprribeiro.app_ai_crafter.repository;

import com.mprribeiro.app_ai_crafter.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {

    List<Plan> findAllByActiveTrue();

    Optional<Plan> findByStripePriceId(final String stripePriceId);
}
