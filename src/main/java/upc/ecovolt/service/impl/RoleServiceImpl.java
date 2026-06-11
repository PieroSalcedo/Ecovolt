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

@Service
@RequiredArgsConstructor
@Slf4j
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
        return roleRepository.findByName(name).map(roleMapper::toResponseDto);
    }

    @Override
    @Transactional
    public RoleDto.Response save(RoleDto.Request requestDto) {
        String roleName = requestDto.getName().toUpperCase().trim();
        if (!roleName.startsWith("ROLE_")) {
            roleName = "ROLE_" + roleName;
        }

        if (roleRepository.findByName(roleName).isPresent()) {
            log.error("INTENTO FALLIDO: El rol {} ya existe en el sistema.", roleName);
            throw new RuntimeException("Error: El perfil de seguridad ya esta registrado.");
        }

        log.info("SISTEMA: Creando nuevo privilegio de acceso: {}", roleName);

        Role entity = new Role();
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
