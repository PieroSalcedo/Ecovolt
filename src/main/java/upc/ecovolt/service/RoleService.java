package upc.ecovolt.service;

import org.springframework.security.access.prepost.PreAuthorize;
import upc.ecovolt.mapping.dto.RoleDto;
import java.util.List;
import java.util.Optional;

public interface RoleService {

    @PreAuthorize("hasAnyRole('ADMIN', 'AUDITOR')")
    List<RoleDto.Response> findAll();

    @PreAuthorize("hasRole('ADMIN')")
    Optional<RoleDto.Response> findByName(String name);

    @PreAuthorize("hasRole('ADMIN')") // REGLA CRÍTICA: Solo el dueño de la App crea roles
    RoleDto.Response save(RoleDto.Request requestDto);

    // REGLA DE NEGOCIO: Reporte para Analistas y Admins
    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST')")
    long countUsersByRoleId(Integer idRol);

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    List<RoleDto.Response> findActiveRoles();
}
