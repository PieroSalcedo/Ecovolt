package upc.ecovolt.mapping.dto.roledto;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import upc.ecovolt.entity.Role;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface RoleMapper {

    RoleResponseDto toResponseDto(Role entity);

    Role toEntity(RoleRequestDto requestDto);

    List<RoleResponseDto> toResponseDtoList(List<Role> entityList);
}