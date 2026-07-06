package upc.ecovolt.mapping.dto;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import upc.ecovolt.entity.Role;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleMapper extends GenericMapper<Role, RoleDto.Request, RoleDto.Response> {
    // Mapeo directo: idRole, name, description y status coinciden
}
