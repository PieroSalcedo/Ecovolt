package upc.ecovolt.service;

import upc.ecovolt.mapping.dto.EnergyAdvisorDto;

public interface EnergyAdvisorService {

    EnergyAdvisorDto.Response analyze(EnergyAdvisorDto.Request request);
}
