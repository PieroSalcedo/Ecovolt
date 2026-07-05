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

@Slf4j
@Service
@RequiredArgsConstructor
public class EnergyGoalServiceImpl implements EnergyGoalService {

    private final EnergyGoalRepository goalRepository;
    private final HomeRepository homeRepository;
    private final EnergyGoalMapper goalMapper;

    /**
     * CIBERSEGURIDAD: Valida si el usuario logueado es el dueño de la casa vinculada a la meta.
     */
    private void validateHomeOwnership(Long idHome) {
        var principal = (UsuarioPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean isAdmin = principal.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            Home home = homeRepository.findById(idHome)
                    .orElseThrow(() -> new RuntimeException("Error: Propiedad no encontrada."));

            if (!home.getUser().getIdUser().equals(principal.getIdUser())) {
                log.error("INTENTO DE FRAUDE: Usuario {} intentó acceder a metas de la Casa ID: {}",
                        principal.getLogin(), idHome);
                throw new RuntimeException("Acceso Denegado: No tienes permisos sobre esta vivienda.");
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public EnergyGoalDto.Response findActiveByTypeAndId(String type, Long id) {
        Optional<EnergyGoal> goalOpt = Optional.empty();

        // Lógica de decisión según el nivel
        if ("CASA".equalsIgnoreCase(type)) {
            goalOpt = goalRepository.findActiveByHome(id);
        } else if ("CUARTO".equalsIgnoreCase(type)) {
            goalOpt = goalRepository.findActiveByRoom(id);
        } else if ("DISPOSITIVO".equalsIgnoreCase(type)) {
            goalOpt = goalRepository.findActiveByDevice(id);
        }

        // Si existe la meta, la mapeamos a DTO. Si no, lanzamos error para que el Front sepa.
        return goalOpt.map(goalMapper::toResponseDto)
                .orElseThrow(() -> new RuntimeException("No se encontró una meta activa para este nivel."));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnergyGoalDto.Response> findAll() {
        return goalMapper.toResponseDtoList(goalRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EnergyGoalDto.Response> findById(Integer idGoal) {
        EnergyGoal goal = goalRepository.findById(idGoal)
                .orElseThrow(() -> new RuntimeException("Meta no encontrada."));

        validateHomeOwnership(goal.getHome().getIdHome());
        return Optional.of(goalMapper.toResponseDto(goal));
    }

    @Override
    @Transactional
    public EnergyGoalDto.Response save(EnergyGoalDto.Request requestDto) {
        // 1. CIBERSEGURIDAD: Validar propiedad de la casa
        validateHomeOwnership(requestDto.getIdHome());

        Home home = homeRepository.findById(requestDto.getIdHome())
                .orElseThrow(() -> new RuntimeException("Casa no encontrada."));

        log.info("REGISTRO META: Estableciendo límite de {} kWh para '{}'",
                requestDto.getTargetValue(), home.getAlias());

        // 2. Mapeo y persistencia
        EnergyGoal entity = goalMapper.toEntity(requestDto);
        entity.setHome(home);
        entity.setStatus(1); // Activa por defecto

        return goalMapper.toResponseDto(goalRepository.save(entity));
    }

    @Override
    @Transactional
    public EnergyGoalDto.Response update(Integer idGoal, EnergyGoalDto.Request requestDto) {
        EnergyGoal existing = goalRepository.findById(idGoal)
                .orElseThrow(() -> new RuntimeException("Meta no encontrada."));

        validateHomeOwnership(existing.getHome().getIdHome());

        // Actualizamos campos según tu EnergyGoalDto.Request (targetValue es monthlyLimitKwh)
        existing.setMonthlyLimitKwh(requestDto.getTargetValue());
        // El alertThreshold se puede manejar aquí si el DTO lo incluyera o usar un valor por defecto

        return goalMapper.toResponseDto(goalRepository.save(existing));
    }

    @Override
    @Transactional
    public void delete(Integer idGoal) {
        EnergyGoal existing = goalRepository.findById(idGoal)
                .orElseThrow(() -> new RuntimeException("Meta no encontrada."));

        validateHomeOwnership(existing.getHome().getIdHome());

        // REGLA DE NEGOCIO: Borrado lógico para mantener historial de comportamiento
        existing.setStatus(0);
        goalRepository.save(existing);
        log.info("META DESACTIVADA: Meta ID {} marcada como inactiva", idGoal);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnergyGoalDto.Response> findActiveGoalsByHome(Long idHome) {
        validateHomeOwnership(idHome);
        // Usamos el método mejorado del repositorio (status = 1)
        return goalMapper.toResponseDtoList(goalRepository.findByHome_IdHomeAndStatus(idHome, 1));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnergyGoalDto.Response> findCriticalGoals(Integer threshold) {
        // REGLA DE NEGOCIO: Reporte administrativo para detectar hogares con riesgo de sobreconsumo
        return goalMapper.toResponseDtoList(goalRepository.findByAlertThresholdPercentageGreaterThanEqual(threshold));
    }
}