package upc.ecovolt.service;

import upc.ecovolt.mapping.dto.energyreadingdto.EnergyReadingRequestDto;
import upc.ecovolt.mapping.dto.energyreadingdto.EnergyReadingResponseDto;

import java.util.List;
import java.util.Optional;

public interface EnergyReadingService {
    List<EnergyReadingResponseDto> findAllReadings();
    Optional<EnergyReadingResponseDto> findReadingById(Long id);
    EnergyReadingResponseDto saveReading(EnergyReadingRequestDto requestDto);
    void delete(Long id);
}
