package upc.ecovolt.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import upc.ecovolt.entity.Role;
import upc.ecovolt.mapping.dto.RoleDto;
import upc.ecovolt.mapping.dto.RoleMapper;
import upc.ecovolt.repository.RoleRepository;
import upc.ecovolt.service.RoleService;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Override
    @Transactional(readOnly = true)
    public List<RoleDto.Response> findAll() {
        return roleMapper.toResponseDtoList(roleRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RoleDto.Response> findByName(String name) {
        return roleRepository.findByName(name.toUpperCase().trim())
                .map(roleMapper::toResponseDto);
    }

    @Override
    @Transactional
    public RoleDto.Response save(RoleDto.Request requestDto) {
        // 1. NORMALIZACIÓN: Asegurar formato ROLE_NOMBRE
        String roleName = requestDto.getName().toUpperCase().trim();
        if (!roleName.startsWith("ROLE_")) {
            roleName = "ROLE_" + roleName;
        }

        // 2. VALIDACIÓN: Evitar duplicados
        if (roleRepository.existsByName(roleName)) {
            log.warn("NEGOCIO: Intento de duplicar rol {}", roleName);
            throw new RuntimeException("El rol ya existe en el sistema.");
        }

        log.info("SISTEMA: Creando nuevo rol de acceso: {}", roleName);

        Role entity = roleMapper.toEntity(requestDto);
        entity.setName(roleName);
        entity.setStatus(1);

        return roleMapper.toResponseDto(roleRepository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public long countUsersByRoleId(Integer idRole) {
        return roleRepository.countUsersByRoleId(idRole);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleDto.Response> findActiveRoles() {
        return roleMapper.toResponseDtoList(roleRepository.findActiveRoles());
    }
}