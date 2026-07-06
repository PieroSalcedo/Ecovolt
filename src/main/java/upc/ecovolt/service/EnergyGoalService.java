package upc.ecovolt.service;

import upc.ecovolt.mapping.dto.EnergyGoalDto;

import java.util.List;
import java.util.Optional;

public interface EnergyGoalService {

    List<EnergyGoalDto.Response> findAll();

    EnergyGoalDto.Response findActiveByTypeAndId(String type, Long id);

    List<EnergyGoalDto.Response> findCriticalGoals(Integer threshold);

    Optional<EnergyGoalDto.Response> findById(Integer idGoal);

    EnergyGoalDto.Response save(EnergyGoalDto.Request requestDto);

    EnergyGoalDto.Response update(Integer idGoal, EnergyGoalDto.Request requestDto);

    void delete(Integer idGoal);

    List<EnergyGoalDto.Response> findActiveGoalsByHome(Long idHome);
}