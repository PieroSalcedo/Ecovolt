package upc.ecovolt.service;

import org.springframework.security.access.prepost.PreAuthorize;
import upc.ecovolt.mapping.dto.subscriptionplandto.SubscriptionPlanRequestDto;
import upc.ecovolt.mapping.dto.subscriptionplandto.SubscriptionPlanResponseDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface SubscriptionPlanService {

    // --- ACCESO PÚBLICO / CUALQUIER ROL ---

    @PreAuthorize("permitAll()")
    List<SubscriptionPlanResponseDto> findAllPlans();

    @PreAuthorize("permitAll()")
    Optional<SubscriptionPlanResponseDto> findPlanById(Integer id);

    @PreAuthorize("permitAll()")
    List<SubscriptionPlanResponseDto> findPlansByPriceRange(BigDecimal min, BigDecimal max);

    /*
     * ESTE ES EL MÉTODO QUE FALTABA Y CAUSABA EL ERROR ROJO
     */
    @PreAuthorize("permitAll()")
    List<SubscriptionPlanResponseDto> findBySupportLevelName(String supportLevel);

    // --- ACCESO EXCLUSIVO STAFF (GESTIÓN) ---

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    SubscriptionPlanResponseDto savePlan(SubscriptionPlanRequestDto requestDto);

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    SubscriptionPlanResponseDto updatePlan(Integer id, SubscriptionPlanRequestDto requestDto);

    @PreAuthorize("hasRole('ADMIN')")
    void delete(Integer id);

    // --- ACCESO ANALÍTICO ---

    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST')")
    long countActiveUsersByPlan(Integer planId);

    // --- LÓGICA DE APOYO AL CLIENTE ---

    @PreAuthorize("isAuthenticated()")
    List<SubscriptionPlanResponseDto> findUpgradeOptions(Integer currentLimit);

    @PreAuthorize("isAuthenticated()")
    Integer getDeviceLimitById(Integer planId);
}