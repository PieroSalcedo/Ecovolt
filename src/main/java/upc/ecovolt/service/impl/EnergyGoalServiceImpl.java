package upc.ecovolt.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import upc.ecovolt.entity.EnergyGoal;
import upc.ecovolt.mapping.dto.energygoaldto.EnergyGoalMapper;
import upc.ecovolt.mapping.dto.energygoaldto.EnergyGoalRequestDto;
import upc.ecovolt.mapping.dto.energygoaldto.EnergyGoalResponseDto;
import upc.ecovolt.repository.EnergyGoalRepository;
import upc.ecovolt.repository.HomeRepository;
import upc.ecovolt.service.EnergyGoalService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EnergyGoalServiceImpl implements EnergyGoalService {

    private final EnergyGoalRepository goalRepository;
    private final HomeRepository homeRepository; // Para validar la jerarquía
    private final EnergyGoalMapper goalMapper;

    @Override
    @Transactional(readOnly = true)
    public List<EnergyGoalResponseDto> findAll() {
        return goalMapper.toResponseDtoList(goalRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EnergyGoalResponseDto> findById(Integer id) {
        return goalRepository.findById(id).map(goalMapper::toResponseDto);
    }

    @Override
    @Transactional
    public EnergyGoalResponseDto save(EnergyGoalRequestDto requestDto) {
        // REGLA DE NEGOCIO: Validar que la casa exista
        var home = homeRepository.findById(requestDto.getHomeId())
                .orElseThrow(() -> new RuntimeException("Error: La vivienda no existe."));

        log.info("Estableciendo meta de ahorro para la casa: {}", home.getAlias());

        EnergyGoal entity = goalMapper.toEntity(requestDto);
        entity.setHome(home);

        return goalMapper.toResponseDto(goalRepository.save(entity));
    }

    @Override
    @Transactional
    public EnergyGoalResponseDto update(Integer id, EnergyGoalRequestDto requestDto) {
        return goalRepository.findById(id).map(existingGoal -> {
            existingGoal.setMonthlyLimitKwh(requestDto.getMonthlyLimitKwh());
            existingGoal.setAlertThresholdPercentage(requestDto.getAlertThresholdPercentage());
            return goalMapper.toResponseDto(goalRepository.save(existingGoal));
        }).orElseThrow(() -> new RuntimeException("Meta no encontrada con ID: " + id));
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        goalRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnergyGoalResponseDto> findActiveGoalsByHome(Long idHome) {
        var goals = goalRepository.findActiveGoalsByHome(idHome);
        return goalMapper.toResponseDtoList(goals);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnergyGoalResponseDto> findCriticalGoals(Integer threshold) {
        // REGLA DE NEGOCIO: Reporte para el sistema de notificaciones
        var goals = goalRepository.findCriticalGoals(threshold);
        return goalMapper.toResponseDtoList(goals);
    }
}