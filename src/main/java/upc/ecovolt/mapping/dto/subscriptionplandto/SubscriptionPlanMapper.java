package upc.ecovolt.mapping.dto.subscriptionplandto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import upc.ecovolt.entity.SubscriptionPlan;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE // <--- IMPORTANTE
)
public interface SubscriptionPlanMapper {

    // 1. De la base de datos hacia el Frontend (Para mostrar datos)
    SubscriptionPlanResponseDto toResponseDto(SubscriptionPlan entity);

    // 2. Del Frontend hacia la base de datos (Para crear/guardar)
    SubscriptionPlan toEntity(SubscriptionPlanRequestDto requestDto);

    // 3. Para listar todos los planes (Convierte la lista completa de una vez)
    List<SubscriptionPlanResponseDto> toResponseDtoList(List<SubscriptionPlan> entityList);
}
