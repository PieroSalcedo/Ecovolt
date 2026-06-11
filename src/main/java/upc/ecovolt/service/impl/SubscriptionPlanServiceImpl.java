package upc.ecovolt.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import upc.ecovolt.entity.SubscriptionPlan;
import upc.ecovolt.mapping.dto.SubscriptionPlanDto;
import upc.ecovolt.mapping.dto.SubscriptionPlanMapper;
import upc.ecovolt.repository.DataCatalogRepository;
import upc.ecovolt.repository.SubscriptionPlanRepository;
import upc.ecovolt.service.SubscriptionPlanService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionPlanServiceImpl implements SubscriptionPlanService {

    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final DataCatalogRepository dataCatalogRepository;
    private final SubscriptionPlanMapper subscriptionPlanMapper;

    @Override
    @Transactional(readOnly = true)
    public List<SubscriptionPlanDto.Response> findAllPlans() {
        // Trae todos los planes (incluyendo inactivos para gestión de Staff)
        return subscriptionPlanMapper.toResponseDtoList(subscriptionPlanRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SubscriptionPlanDto.Response> findPlanById(Integer idPlan) {
        return subscriptionPlanRepository.findById(idPlan)
                .map(subscriptionPlanMapper::toResponseDto);
    }

    @Override
    @Transactional
    public SubscriptionPlanDto.Response savePlan(SubscriptionPlanDto.Request requestDto) {
        log.info("NEGOCIO: Registrando nuevo plan comercial: {}", requestDto.getName());

        SubscriptionPlan entity = subscriptionPlanMapper.toEntity(requestDto);

        // REGLA DE INTEGRIDAD: Si el DTO trae un supportLevelId, lo vinculamos al catálogo
        // Nota: Asegúrate de que el RequestDto tenga este campo.
        /*
        var supportLevel = dataCatalogRepository.findById(requestDto.getSupportLevelId())
                .orElseThrow(() -> new RuntimeException("El nivel de soporte especificado no existe."));
        entity.setSupportLevel(supportLevel);
        */

        entity.setStatus(1); // Activo por defecto

        return subscriptionPlanMapper.toResponseDto(subscriptionPlanRepository.save(entity));
    }

    @Override
    @Transactional
    public SubscriptionPlanDto.Response updatePlan(Integer idPlan, SubscriptionPlanDto.Request requestDto) {
        return subscriptionPlanRepository.findById(idPlan).map(existing -> {
            existing.setName(requestDto.getName());
            // MapStruct suele manejar esto, pero si se hace manual:
            // existing.setMonthlyPrice(requestDto.getPrice());
            // existing.setDeviceLimit(requestDto.getDeviceLimit());

            return subscriptionPlanMapper.toResponseDto(subscriptionPlanRepository.save(existing));
        }).orElseThrow(() -> new RuntimeException("Plan no encontrado con ID: " + idPlan));
    }

    @Override
    @Transactional
    public void delete(Integer idPlan) {
        // REGLA DE NEGOCIO: Borrado lógico para no afectar usuarios ya suscritos (SaaS Integrity)
        subscriptionPlanRepository.findById(idPlan).ifPresent(plan -> {
            plan.setStatus(0);
            subscriptionPlanRepository.save(plan);
            log.warn("BORRADO LÓGICO: Plan ID {} desactivado", idPlan);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubscriptionPlanDto.Response> findPlansByPriceRange(BigDecimal min, BigDecimal max) {
        return subscriptionPlanMapper.toResponseDtoList(subscriptionPlanRepository.findPlansByPriceRange(min, max));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubscriptionPlanDto.Response> findBySupportLevelName(String supportLevel) {
        return subscriptionPlanMapper.toResponseDtoList(subscriptionPlanRepository.findBySupportLevelName(supportLevel));
    }

    @Override
    @Transactional(readOnly = true)
    public long countActiveUsersByPlan(Integer idPlan) {
        // REGLA DE NEGOCIO: Analítica de tracción del producto
        return subscriptionPlanRepository.countActiveUsersByPlan(idPlan);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubscriptionPlanDto.Response> findUpgradeOptions(Integer currentLimit) {
        // Lógica de Upselling: Mostrar planes mejores que el actual
        return subscriptionPlanMapper.toResponseDtoList(subscriptionPlanRepository.findUpgradeOptions(currentLimit));
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getDeviceLimitById(Integer idPlan) {
        // Uso de Optional mejorado en el Repo para evitar Nulls
        return subscriptionPlanRepository.getDeviceLimitById(idPlan)
                .orElseThrow(() -> new RuntimeException("No se encontró el límite para el plan especificado."));
    }
}