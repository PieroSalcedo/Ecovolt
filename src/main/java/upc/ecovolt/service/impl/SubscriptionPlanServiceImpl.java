package upc.ecovolt.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import upc.ecovolt.entity.SubscriptionPlan;
import upc.ecovolt.mapping.dto.subscriptionplandto.SubscriptionPlanMapper;
import upc.ecovolt.mapping.dto.subscriptionplandto.SubscriptionPlanRequestDto;
import upc.ecovolt.mapping.dto.subscriptionplandto.SubscriptionPlanResponseDto;
import upc.ecovolt.repository.DataCatalogoRepository; // Necesario para resolver el catálogo
import upc.ecovolt.repository.SubscriptionPlanRepository;
import upc.ecovolt.service.SubscriptionPlanService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriptionPlanServiceImpl implements SubscriptionPlanService {

    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final DataCatalogoRepository dataCatalogoRepository;
    private final SubscriptionPlanMapper subscriptionPlanMapper;

    @Override
    @Transactional(readOnly = true)
    public List<SubscriptionPlanResponseDto> findAllPlans() {
        return subscriptionPlanMapper.toResponseDtoList(subscriptionPlanRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SubscriptionPlanResponseDto> findPlanById(Integer id) {
        return subscriptionPlanRepository.findById(id)
                .map(subscriptionPlanMapper::toResponseDto);
    }

    @Override
    @Transactional
    public SubscriptionPlanResponseDto savePlan(SubscriptionPlanRequestDto requestDto) {
        log.info("Creando nuevo plan de suscripción: {}", requestDto.getName());

        SubscriptionPlan entity = subscriptionPlanMapper.toEntity(requestDto);

        // REGLA TÉCNICA: Buscamos el DataCatalogo por el ID que viene en el DTO
        var supportLevel = dataCatalogoRepository.findById(requestDto.getSupportLevelId())
                .orElseThrow(() -> new RuntimeException("Nivel de soporte no encontrado"));

        entity.setSupportLevel(supportLevel);

        var savedEntity = subscriptionPlanRepository.save(entity);
        return subscriptionPlanMapper.toResponseDto(savedEntity);
    }

    @Override
    @Transactional
    public SubscriptionPlanResponseDto updatePlan(Integer id, SubscriptionPlanRequestDto requestDto) {
        return subscriptionPlanRepository.findById(id).map(existingPlan -> {
            existingPlan.setName(requestDto.getName());
            existingPlan.setMonthlyPrice(requestDto.getMonthlyPrice());
            existingPlan.setDeviceLimit(requestDto.getDeviceLimit());
            existingPlan.setBillingCycle(requestDto.getBillingCycle());

            // Actualizamos el catálogo si ha cambiado
            var supportLevel = dataCatalogoRepository.findById(requestDto.getSupportLevelId())
                    .orElseThrow(() -> new RuntimeException("Nivel de soporte no encontrado"));
            existingPlan.setSupportLevel(supportLevel);

            return subscriptionPlanMapper.toResponseDto(subscriptionPlanRepository.save(existingPlan));
        }).orElseThrow(() -> new RuntimeException("Plan no encontrado con ID: " + id));
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        if (!subscriptionPlanRepository.existsById(id)) {
            throw new RuntimeException("El plan no existe.");
        }
        subscriptionPlanRepository.deleteById(id);
        log.warn("Plan con ID {} eliminado", id);
    }

    // --- IMPLEMENTACIÓN DE MÉTODOS DE NEGOCIO ---

    @Override
    @Transactional(readOnly = true)
    public List<SubscriptionPlanResponseDto> findPlansByPriceRange(BigDecimal min, BigDecimal max) {
        var plans = subscriptionPlanRepository.findPlansByPriceRange(min, max);
        return subscriptionPlanMapper.toResponseDtoList(plans);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubscriptionPlanResponseDto> findBySupportLevelName(String supportLevel) {
        // Usa la lógica del JOIN con DataCatalogo que definimos en el Repo
        var plans = subscriptionPlanRepository.findBySupportLevelName(supportLevel);
        return subscriptionPlanMapper.toResponseDtoList(plans);
    }

    @Override
    @Transactional(readOnly = true)
    public long countActiveUsersByPlan(Integer planId) {
        return subscriptionPlanRepository.countActiveUsersByPlan(planId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubscriptionPlanResponseDto> findUpgradeOptions(Integer currentLimit) {
        log.info("Buscando opciones de Upselling para límite actual: {}", currentLimit);
        var plans = subscriptionPlanRepository.findUpgradeOptions(currentLimit);
        return subscriptionPlanMapper.toResponseDtoList(plans);
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getDeviceLimitById(Integer planId) {
        return subscriptionPlanRepository.getDeviceLimitById(planId);
    }
}