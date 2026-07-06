package upc.ecovolt.service;

import upc.ecovolt.mapping.dto.RoleDto;
import java.util.List;
import java.util.Optional;

public interface RoleService {

    List<RoleDto.Response> findAll();

    Optional<RoleDto.Response> findByName(String name);

    RoleDto.Response save(RoleDto.Request requestDto);

    /*
     * REGLA DE NEGOCIO: Analítica de población por rol.
     * Útil para el Dashboard administrativo.
     */
    long countUsersByRoleId(Integer idRole);

    List<RoleDto.Response> findActiveRoles();

    /*
     * NOTA: La seguridad (quién puede ver qué) la manejaremos
     * mediante la relación con la tabla 'Option' en el SecurityConfig.
     */
}