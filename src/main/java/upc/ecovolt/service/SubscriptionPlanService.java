package upc.ecovolt.service;

import upc.ecovolt.mapping.dto.subscriptionplandto.SubscriptionPlanRequestDto;
import upc.ecovolt.mapping.dto.subscriptionplandto.SubscriptionPlanResponseDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface SubscriptionPlanService {
    // CRUD Básico
    List<SubscriptionPlanResponseDto> findAllPlans();
    Optional<SubscriptionPlanResponseDto> findPlanById(Integer id);
    SubscriptionPlanResponseDto savePlan(SubscriptionPlanRequestDto requestDto);
    SubscriptionPlanResponseDto updatePlan(Integer id, SubscriptionPlanRequestDto requestDto);
    void delete(Integer id);

    // REGLAS DE NEGOCIO (Provenientes del Repositorio)
    List<SubscriptionPlanResponseDto> findPlansByPriceRange(BigDecimal min, BigDecimal max);
    List<SubscriptionPlanResponseDto> findBySupportLevelName(String supportLevel);
    long countActiveUsersByPlan(Integer planId);
    List<SubscriptionPlanResponseDto> findUpgradeOptions(Integer currentLimit);
    Integer getDeviceLimitById(Integer planId);
}