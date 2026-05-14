package upc.ecovolt.mapping.dto.subscriptionplandto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import upc.ecovolt.entity.SubscriptionPlan;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SubscriptionPlanMapper {

    // ENTITY -> RESPONSE (Mostramos la descripción del catálogo)
    @Mapping(target = "supportLevel", source = "supportLevel.description")
    SubscriptionPlanResponseDto toResponseDto(SubscriptionPlan entity);

    // REQUEST -> ENTITY (Ignoramos supportLevel porque se asignará por ID en el Service)
    @Mapping(target = "supportLevel", ignore = true)
    SubscriptionPlan toEntity(SubscriptionPlanRequestDto requestDto);

    // Mapeo de listas para el listar todo
    List<SubscriptionPlanResponseDto> toResponseDtoList(List<SubscriptionPlan> entityList);
}