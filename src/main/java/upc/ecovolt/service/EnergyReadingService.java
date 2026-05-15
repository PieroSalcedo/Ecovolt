package upc.ecovolt.service;

import org.springframework.security.access.prepost.PreAuthorize;
import upc.ecovolt.mapping.dto.energyreadingdto.EnergyReadingRequestDto;
import upc.ecovolt.mapping.dto.energyreadingdto.EnergyReadingResponseDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EnergyReadingService {

    // --- ACCESO ADMINISTRATIVO (STAFF) ---
    @PreAuthorize("hasAnyRole('ADMIN', 'AUDITOR')")
    List<EnergyReadingResponseDto> findAllReadings();

    @PreAuthorize("hasRole('ADMIN')")
    void delete(Long id);

    // --- ACCESO POR PROPIEDAD (OWNERSHIP / ANALYTICS) ---

    @PreAuthorize("isAuthenticated()")
    Optional<EnergyReadingResponseDto> findReadingById(Long id);

    @PreAuthorize("isAuthenticated()") // La validación de que el sensor es suyo se hace en el Impl
    EnergyReadingResponseDto saveReading(EnergyReadingRequestDto requestDto);

    @PreAuthorize("isAuthenticated()")
    BigDecimal sumWattageByDeviceAndPeriod(Long idDevice, LocalDateTime start, LocalDateTime end);

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPPORT') or isAuthenticated()")
    Double getAverageVoltageByDevice(Long idDevice);

    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST') or isAuthenticated()")
    BigDecimal sumTotalConsumptionByHome(Long idHome, LocalDateTime start, LocalDateTime end);

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPPORT') or isAuthenticated()")
    List<EnergyReadingResponseDto> findLatestReadingsByDevice(Long idDevice);

    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST') or isAuthenticated()")
    List<EnergyReadingResponseDto> findAbnormalConsumption(Long idDevice, BigDecimal threshold);

    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST') or isAuthenticated()")
    BigDecimal sumConsumptionByCategory(Long idHome, String categoryDescription, LocalDateTime start, LocalDateTime end);
}