package upc.ecovolt.service;

import upc.ecovolt.mapping.dto.subscriptionplandto.SubscriptionPlanRequestDto;
import upc.ecovolt.mapping.dto.subscriptionplandto.SubscriptionPlanResponseDto;

import java.util.List;
import java.util.Optional;

public interface SubscriptionPlanService {
    List<SubscriptionPlanResponseDto> findAllPlans();

    // 2. El Optional ahora envuelve al DTO de respuesta
    Optional<SubscriptionPlanResponseDto> findPlanById(Long id);

    // 3. Recibimos los datos de creación (Request) y devolvemos el resultado limpio (Response)
    SubscriptionPlanResponseDto savePlan(SubscriptionPlanRequestDto objSubRequestDto);

    // 4. Para el update, recibimos el ID y el RequestDto
    SubscriptionPlanResponseDto updatePlan(Long id, SubscriptionPlanRequestDto objSubRequestDto);

    void delete(Long id);
}
