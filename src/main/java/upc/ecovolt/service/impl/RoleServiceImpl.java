package upc.ecovolt.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import upc.ecovolt.entity.Role;
import upc.ecovolt.mapping.dto.roledto.RoleMapper;
import upc.ecovolt.mapping.dto.roledto.RoleRequestDto;
import upc.ecovolt.mapping.dto.roledto.RoleResponseDto;
import upc.ecovolt.repository.RoleRepository;
import upc.ecovolt.service.RoleService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Override
    @Transactional(readOnly = true)
    public List<RoleResponseDto> findAll() {
        return roleMapper.toResponseDtoList(roleRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RoleResponseDto> findByNombre(String nombre) {
        return roleRepository.findByNombre(nombre).map(roleMapper::toResponseDto);
    }

    @Override
    @Transactional
    public RoleResponseDto save(RoleRequestDto requestDto) {
        // 1. REGLA DE SEGURIDAD: Normalizar el nombre (Mayúsculas y Prefijo ROLE_)
        String roleName = requestDto.getNombre().toUpperCase().trim();
        if (!roleName.startsWith("ROLE_")) {
            roleName = "ROLE_" + roleName;
        }

        // 2. VALIDACIÓN: Evitar duplicidad de identidades de acceso
        if (roleRepository.findByNombre(roleName).isPresent()) {
            log.error("INTENTO FALLIDO: El rol {} ya existe en el sistema.", roleName);
            throw new RuntimeException("Error: El perfil de seguridad ya está registrado.");
        }

        log.info("SISTEMA: Creando nuevo privilegio de acceso: {}", roleName);

        Role entity = new Role();
        entity.setNombre(roleName);
        entity.setEstado(1); // Activo por defecto

        return roleMapper.toResponseDto(roleRepository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public long countUsersByRoleId(Integer idRol) {
        // REGLA DE NEGOCIO: Analítica de distribución de usuarios
        return roleRepository.countUsersByRoleId(idRol);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleResponseDto> findActiveRoles() {
        return roleMapper.toResponseDtoList(roleRepository.findActiveRoles());
    }
}