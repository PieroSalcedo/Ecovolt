package upc.ecovolt.mapping.dto.userdto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import upc.ecovolt.entity.User;
import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UserMapper {

    // ENTITY -> DTO
    @Mapping(target = "subscriptionPlanId", source = "subscriptionPlan.id")
    @Mapping(target = "subscriptionPlanName", source = "subscriptionPlan.name") // <--- REGLA: Mapea el nombre del plan
    UserResponseDto toDto(User user);

    // DTO -> ENTITY
    @Mapping(target = "subscriptionPlan.id", source = "subscriptionPlanId")
    @Mapping(target = "password", ignore = true) // REGLA: La contraseña se encripta en el Service, no aquí
    User toEntity(UserRequestDto userRequestDto);

    List<UserResponseDto> toDtoList(List<User> userList);
}