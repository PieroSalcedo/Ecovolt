package upc.ecovolt.service;

import upc.ecovolt.mapping.dto.energyreadingdto.EnergyReadingRequestDto;
import upc.ecovolt.mapping.dto.energyreadingdto.EnergyReadingResponseDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EnergyReadingService {
    // CRUD Básico (Ligero)
    List<EnergyReadingResponseDto> findAllReadings();
    Optional<EnergyReadingResponseDto> findReadingById(Long id);
    EnergyReadingResponseDto saveReading(EnergyReadingRequestDto requestDto);
    void delete(Long id);

    // MÉTODOS DE ANALÍTICA (Provenientes del Repositorio)
    BigDecimal sumWattageByDeviceAndPeriod(Long idDevice, LocalDateTime start, LocalDateTime end);
    Double getAverageVoltageByDevice(Long idDevice);
    BigDecimal sumTotalConsumptionByHome(Long idHome, LocalDateTime start, LocalDateTime end);
    List<EnergyReadingResponseDto> findLatestReadingsByDevice(Long idDevice);
    List<EnergyReadingResponseDto> findAbnormalConsumption(Long idDevice, BigDecimal threshold);
    BigDecimal sumConsumptionByCategory(Long idHome, String categoryDescription, LocalDateTime start, LocalDateTime end);
}