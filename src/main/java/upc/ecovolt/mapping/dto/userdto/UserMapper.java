package upc.ecovolt.mapping.dto.userdto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import upc.ecovolt.entity.User;
import java.util.List;

// unmappedTargetPolicy = IGNORE evita warnings por los campos de auditoría de BaseEntity
@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE // <--- IMPORTANTE
)
public interface UserMapper {

    // 1. Entity -> DTO: Mapeamos el ID del objeto SubscriptionPlan al Long del DTO
    @Mapping(target = "subscriptionPlanId", source = "subscriptionPlan.id")
    UserResponseDto toDto(User user);

    // 2. DTO -> Entity: Mapeamos el Long del DTO al ID dentro del objeto SubscriptionPlan
    @Mapping(target = "subscriptionPlan.id", source = "subscriptionPlanId")
    User toEntity(UserRequestDto userRequestDto);

    // 3. Mapeo de listas para el findAll
    List<UserResponseDto> toDtoList(List<User> userList);
}