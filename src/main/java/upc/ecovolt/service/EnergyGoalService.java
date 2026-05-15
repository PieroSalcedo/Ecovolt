package upc.ecovolt.service;

import org.springframework.security.access.prepost.PreAuthorize;
import upc.ecovolt.mapping.dto.energygoaldto.EnergyGoalRequestDto;
import upc.ecovolt.mapping.dto.energygoaldto.EnergyGoalResponseDto;

import java.util.List;
import java.util.Optional;

public interface EnergyGoalService {

    // --- ACCESO ADMINISTRATIVO (STAFF) ---
    @PreAuthorize("hasAnyRole('ADMIN', 'AUDITOR', 'ANALYST')")
    List<EnergyGoalResponseDto> findAll();

    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST')")
    List<EnergyGoalResponseDto> findCriticalGoals(Integer threshold);

    // --- ACCESO POR PROPIEDAD (OWNERSHIP) ---

    @PreAuthorize("isAuthenticated()")
    Optional<EnergyGoalResponseDto> findById(Integer id);

    @PreAuthorize("isAuthenticated()") // La validación del dueño de la casa se hace en el Impl
    EnergyGoalResponseDto save(EnergyGoalRequestDto requestDto);

    @PreAuthorize("isAuthenticated()")
    EnergyGoalResponseDto update(Integer id, EnergyGoalRequestDto requestDto);

    @PreAuthorize("isAuthenticated()")
    void delete(Integer id);

    @PreAuthorize("isAuthenticated()")
    List<EnergyGoalResponseDto> findActiveGoalsByHome(Long idHome);
}