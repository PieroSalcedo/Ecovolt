package upc.ecovolt.service;

import upc.ecovolt.mapping.dto.EnergyGoalDto;

import java.util.List;
import java.util.Optional;

public interface EnergyGoalService {

    List<EnergyGoalDto.Response> findAll();

    List<EnergyGoalDto.Response> findCriticalGoals(Integer threshold);

    Optional<EnergyGoalDto.Response> findById(Integer id);

    EnergyGoalDto.Response save(EnergyGoalDto.Request requestDto);

    EnergyGoalDto.Response update(Integer id, EnergyGoalDto.Request requestDto);

    void delete(Integer id);

    List<EnergyGoalDto.Response> findActiveGoalsByHome(Long idHome);
}
