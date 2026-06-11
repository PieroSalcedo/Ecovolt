package upc.ecovolt.service;

import upc.ecovolt.mapping.dto.EnergyReadingDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EnergyReadingService {

    List<EnergyReadingDto.Response> findAllReadings();

    void delete(Long id);

    Optional<EnergyReadingDto.Response> findReadingById(Long id);

    EnergyReadingDto.Response saveReading(EnergyReadingDto.Request requestDto);

    BigDecimal sumWattageByDeviceAndPeriod(Long idDevice, LocalDateTime start, LocalDateTime end);

    Double getAverageVoltageByDevice(Long idDevice);

    BigDecimal sumTotalConsumptionByHome(Long idHome, LocalDateTime start, LocalDateTime end);

    List<EnergyReadingDto.Response> findLatestReadingsByDevice(Long idDevice);

    List<EnergyReadingDto.Response> findAbnormalConsumption(Long idDevice, BigDecimal threshold);

    BigDecimal sumConsumptionByCategory(Long idHome, String categoryDescription, LocalDateTime start, LocalDateTime end);
}
