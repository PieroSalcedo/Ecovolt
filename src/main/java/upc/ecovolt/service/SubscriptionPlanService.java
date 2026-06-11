package upc.ecovolt.service;

import org.springframework.security.access.prepost.PreAuthorize;
import upc.ecovolt.mapping.dto.SubscriptionPlanDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface SubscriptionPlanService {

    // --- ACCESO PÚBLICO / CUALQUIER ROL ---

    @PreAuthorize("permitAll()")
    List<SubscriptionPlanDto.Response> findAllPlans();

    @PreAuthorize("permitAll()")
    Optional<SubscriptionPlanDto.Response> findPlanById(Integer id);

    @PreAuthorize("permitAll()")
    List<SubscriptionPlanDto.Response> findPlansByPriceRange(BigDecimal min, BigDecimal max);

    /*
     * ESTE ES EL MÉTODO QUE FALTABA Y CAUSABA EL ERROR ROJO
     */
    @PreAuthorize("permitAll()")
    List<SubscriptionPlanDto.Response> findBySupportLevelName(String supportLevel);

    // --- ACCESO EXCLUSIVO STAFF (GESTIÓN) ---

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    SubscriptionPlanDto.Response savePlan(SubscriptionPlanDto.Request requestDto);

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    SubscriptionPlanDto.Response updatePlan(Integer id, SubscriptionPlanDto.Request requestDto);

    @PreAuthorize("hasRole('ADMIN')")
    void delete(Integer id);

    // --- ACCESO ANALÍTICO ---

    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST')")
    long countActiveUsersByPlan(Integer planId);

    // --- LÓGICA DE APOYO AL CLIENTE ---

    @PreAuthorize("isAuthenticated()")
    List<SubscriptionPlanDto.Response> findUpgradeOptions(Integer currentLimit);

    @PreAuthorize("isAuthenticated()")
    Integer getDeviceLimitById(Integer planId);
}
