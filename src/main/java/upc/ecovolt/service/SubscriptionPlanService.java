package upc.ecovolt.service;

import upc.ecovolt.mapping.dto.SubscriptionPlanDto;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface SubscriptionPlanService {

    List<SubscriptionPlanDto.Response> findAllPlans();

    Optional<SubscriptionPlanDto.Response> findPlanById(Integer idPlan);

    List<SubscriptionPlanDto.Response> findPlansByPriceRange(BigDecimal min, BigDecimal max);

    List<SubscriptionPlanDto.Response> findBySupportLevelName(String supportLevel);

    SubscriptionPlanDto.Response savePlan(SubscriptionPlanDto.Request requestDto);

    SubscriptionPlanDto.Response updatePlan(Integer idPlan, SubscriptionPlanDto.Request requestDto);

    void delete(Integer idPlan);

    long countActiveUsersByPlan(Integer idPlan);

    List<SubscriptionPlanDto.Response> findUpgradeOptions(Integer currentLimit);

    Integer getDeviceLimitById(Integer idPlan);
}