package upc.ecovolt.service;

import upc.ecovolt.mapping.dto.EnergyAdvisorDto;

import java.util.List;
import java.util.Optional;

public interface GeminiAdvisorService {

    Optional<EnergyAdvisorDto.GeminiPayload> generateAdvice(String context, List<EnergyAdvisorDto.Metric> metrics);
}
