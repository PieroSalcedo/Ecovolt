package upc.ecovolt.service;

import upc.ecovolt.mapping.dto.EnergyGoalDto;

import java.util.List;
import java.util.Optional;

public interface EnergyGoalService {

    List<EnergyGoalDto> findAll();

    List<EnergyGoalDto> findCriticalGoals(Integer threshold);

    Optional<EnergyGoalDto> findById(Integer id);

    EnergyGoalDto save(EnergyGoalDto requestDto);

    EnergyGoalDto update(Integer id, EnergyGoalDto requestDto);

    void delete(Integer id);

    List<EnergyGoalDto> findActiveGoalsByHome(Long idHome);
}