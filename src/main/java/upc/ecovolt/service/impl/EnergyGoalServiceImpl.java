package upc.ecovolt.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import upc.ecovolt.entity.EnergyGoal;
import upc.ecovolt.entity.Home;
import upc.ecovolt.mapping.dto.EnergyGoalDto;
import upc.ecovolt.mapping.dto.EnergyGoalMapper;
import upc.ecovolt.repository.EnergyGoalRepository;
import upc.ecovolt.repository.HomeRepository;
import upc.ecovolt.security.UsuarioPrincipal;
import upc.ecovolt.service.EnergyGoalService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EnergyGoalServiceImpl implements EnergyGoalService {

    private final EnergyGoalRepository goalRepository;
    private final HomeRepository homeRepository;
    private final EnergyGoalMapper goalMapper;

    /**
     * CIBERSEGURIDAD: Valida si el usuario actual es dueño de la CASA vinculada a la meta.
     */
    private void validateHomeOwnership(Long homeId) {
        var principal = (UsuarioPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean isAdmin = principal.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            Home home = homeRepository.findById(homeId)
                    .orElseThrow(() -> new RuntimeException("Error: Propiedad no encontrada."));
            if (!home.getUser().getIdUser().equals(principal.getIdUser())) {
                log.error("FRAUDE DETECTADO: El usuario {} intentó manipular metas de la casa ID: {}",
                        principal.getLogin(), homeId);
                throw new RuntimeException("Acceso Denegado: No tienes permisos sobre esta vivienda.");
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnergyGoalDto.Response> findAll() {
        return goalMapper.toResponseDtoList(goalRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EnergyGoalDto.Response> findById(Integer id) {
        var goal = goalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Meta no encontrada"));
        validateHomeOwnership(goal.getHome().getIdHome());
        return Optional.of(goalMapper.toResponseDto(goal));
    }

    @Override
    @Transactional
    public EnergyGoalDto.Response save(EnergyGoalDto.Request requestDto) {
        // CIBERSEGURIDAD: ¿La casa donde quieres poner la meta es tuya?
        validateHomeOwnership(requestDto.getHomeId());

        var home = homeRepository.findById(requestDto.getHomeId()).get();
        log.info("USER {}: Estableciendo presupuesto de {} kWh para '{}'",
                home.getUser().getLogin(), requestDto.getMonthlyLimitKwh(), home.getAlias());

        EnergyGoal entity = goalMapper.toEntity(requestDto);
        entity.setHome(home);
        entity.setCreatedBy(home.getUser().getLogin());

        return goalMapper.toResponseDto(goalRepository.save(entity));
    }

    @Override
    @Transactional
    public EnergyGoalDto.Response update(Integer id, EnergyGoalDto.Request requestDto) {
        EnergyGoal existing = goalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Meta no encontrada"));

        validateHomeOwnership(existing.getHome().getIdHome());

        existing.setMonthlyLimitKwh(requestDto.getMonthlyLimitKwh());
        existing.setAlertThresholdPercentage(requestDto.getAlertThresholdPercentage());
        existing.setUpdatedBy(SecurityContextHolder.getContext().getAuthentication().getName());

        return goalMapper.toResponseDto(goalRepository.save(existing));
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        EnergyGoal existing = goalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Meta no encontrada"));
        validateHomeOwnership(existing.getHome().getIdHome());
        goalRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnergyGoalDto.Response> findActiveGoalsByHome(Long idHome) {
        validateHomeOwnership(idHome);
        return goalMapper.toResponseDtoList(goalRepository.findActiveGoalsByHome(idHome));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnergyGoalDto.Response> findCriticalGoals(Integer threshold) {
        // REGLA DE NEGOCIO: Reporte para el Staff (DEVIDA)
        // Permite identificar qué sectores están en riesgo de superar sus cuotas
        return goalMapper.toResponseDtoList(goalRepository.findCriticalGoals(threshold));
    }
}
