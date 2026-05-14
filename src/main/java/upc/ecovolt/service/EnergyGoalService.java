package upc.ecovolt.service;

import upc.ecovolt.mapping.dto.energygoaldto.EnergyGoalRequestDto;
import upc.ecovolt.mapping.dto.energygoaldto.EnergyGoalResponseDto;
import java.util.List;
import java.util.Optional;

public interface EnergyGoalService {
    // CRUD
    List<EnergyGoalResponseDto> findAll();
    Optional<EnergyGoalResponseDto> findById(Integer id);
    EnergyGoalResponseDto save(EnergyGoalRequestDto requestDto);
    EnergyGoalResponseDto update(Integer id, EnergyGoalRequestDto requestDto);
    void delete(Integer id);

    // NEGOCIO
    List<EnergyGoalResponseDto> findActiveGoalsByHome(Long idHome);
    List<EnergyGoalResponseDto> findCriticalGoals(Integer threshold);
}