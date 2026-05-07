package upc.ecovolt.service.impl;

import upc.ecovolt.mapping.dto.subscriptionplandto.SubscriptionPlanMapper;
import upc.ecovolt.mapping.dto.subscriptionplandto.SubscriptionPlanRequestDto;
import upc.ecovolt.mapping.dto.subscriptionplandto.SubscriptionPlanResponseDto;
import upc.ecovolt.repository.SubscriptionPlanRepository;
import upc.ecovolt.service.SubscriptionPlanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriptionPlanServiceImpl implements SubscriptionPlanService {

    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final SubscriptionPlanMapper subscriptionPlanMapper;

    @Override
    @Transactional(readOnly = true) // 3. Optimización para consultas
    public List<SubscriptionPlanResponseDto> findAllPlans() {
        var entities = subscriptionPlanRepository.findAll();
        return subscriptionPlanMapper.toResponseDtoList(entities);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SubscriptionPlanResponseDto> findPlanById(Long id) {
        return subscriptionPlanRepository.findById(id)
                .map(subscriptionPlanMapper::toResponseDto);
    }

    @Override
    @Transactional // 4. Asegura la integridad del registro
    public SubscriptionPlanResponseDto savePlan(SubscriptionPlanRequestDto requestDto) {
        log.info("Creando nuevo plan de suscripción: {}", requestDto.getName());

        var entity = subscriptionPlanMapper.toEntity(requestDto);
        var savedEntity = subscriptionPlanRepository.save(entity);

        return subscriptionPlanMapper.toResponseDto(savedEntity);
    }

    @Override
    @Transactional
    public SubscriptionPlanResponseDto updatePlan(Long id, SubscriptionPlanRequestDto requestDto) {
        return subscriptionPlanRepository.findById(id).map(existingPlan -> {
            // Actualizamos solo los campos de negocio necesarios
            existingPlan.setName(requestDto.getName());
            existingPlan.setMonthlyPrice(requestDto.getMonthlyPrice());
            existingPlan.setDeviceLimit(requestDto.getDeviceLimit());
            existingPlan.setSupportLevel(requestDto.getSupportLevel());
            existingPlan.setBillingCycle(requestDto.getBillingCycle());

            var updated = subscriptionPlanRepository.save(existingPlan);
            log.info("Plan con ID {} actualizado correctamente", id);
            return subscriptionPlanMapper.toResponseDto(updated);
        }).orElseThrow(() -> new RuntimeException("Subscription Plan not found with id: " + id));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!subscriptionPlanRepository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar. El plan con ID " + id + " no existe.");
        }
        subscriptionPlanRepository.deleteById(id);
        log.warn("Plan con ID {} ha sido eliminado", id);
    }
}