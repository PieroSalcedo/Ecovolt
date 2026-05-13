package upc.ecovolt.mapping.dto.userroledto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import upc.ecovolt.entity.UserRole;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UserRoleMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "roleId", source = "role.id")
    UserRoleResponseDto toResponseDto(UserRole entity);

    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "role.id", source = "roleId")
    UserRole toEntity(UserRoleRequestDto requestDto);

    List<UserRoleResponseDto> toResponseDtoList(List<UserRole> entityList);
}