package upc.ecovolt.service;

import org.springframework.security.access.prepost.PreAuthorize;
import upc.ecovolt.mapping.dto.roledto.RoleRequestDto;
import upc.ecovolt.mapping.dto.roledto.RoleResponseDto;
import java.util.List;
import java.util.Optional;

public interface RoleService {

    @PreAuthorize("hasAnyRole('ADMIN', 'AUDITOR')")
    List<RoleResponseDto> findAll();

    @PreAuthorize("hasRole('ADMIN')")
    Optional<RoleResponseDto> findByNombre(String nombre);

    @PreAuthorize("hasRole('ADMIN')") // REGLA CRÍTICA: Solo el dueño de la App crea roles
    RoleResponseDto save(RoleRequestDto requestDto);

    // REGLA DE NEGOCIO: Reporte para Analistas y Admins
    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST')")
    long countUsersByRoleId(Integer idRol);

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    List<RoleResponseDto> findActiveRoles();
}