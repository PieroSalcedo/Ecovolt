package upc.ecovolt.service;

import upc.ecovolt.mapping.dto.EnergyReadingDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EnergyReadingService {

    List<EnergyReadingDto> findAllReadings();

    void delete(Long id);

    Optional<EnergyReadingDto> findReadingById(Long id);

    EnergyReadingDto saveReading(EnergyReadingDto requestDto);

    BigDecimal sumWattageByDeviceAndPeriod(Long idDevice, LocalDateTime start, LocalDateTime end);

    Double getAverageVoltageByDevice(Long idDevice);

    BigDecimal sumTotalConsumptionByHome(Long idHome, LocalDateTime start, LocalDateTime end);

    List<EnergyReadingDto> findLatestReadingsByDevice(Long idDevice);

    List<EnergyReadingDto> findAbnormalConsumption(Long idDevice, BigDecimal threshold);

    BigDecimal sumConsumptionByCategory(Long idHome, String categoryDescription, LocalDateTime start, LocalDateTime end);
}