package upc.ecovolt.service;

import upc.ecovolt.mapping.dto.roledto.RoleRequestDto;
import upc.ecovolt.mapping.dto.roledto.RoleResponseDto;
import java.util.List;
import java.util.Optional;

public interface RoleService {
    List<RoleResponseDto> findAll();
    Optional<RoleResponseDto> findByNombre(String nombre);
    RoleResponseDto save(RoleRequestDto requestDto);

    // REGLA DE NEGOCIO
    long countUsersByRoleId(Integer idRol);
    List<RoleResponseDto> findActiveRoles();
}