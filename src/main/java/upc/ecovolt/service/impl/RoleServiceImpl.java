package upc.ecovolt.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import upc.ecovolt.mapping.dto.roledto.RoleMapper;
import upc.ecovolt.mapping.dto.roledto.RoleRequestDto;
import upc.ecovolt.mapping.dto.roledto.RoleResponseDto;
import upc.ecovolt.repository.RoleRepository;
import upc.ecovolt.service.RoleService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
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
        var entity = roleMapper.toEntity(requestDto);
        return roleMapper.toResponseDto(roleRepository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public long countUsersByRoleId(Integer idRol) {
        // REGLA DE NEGOCIO: Reporte administrativo de usuarios por rol
        return roleRepository.countUsersByRoleId(idRol);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleResponseDto> findActiveRoles() {
        return roleMapper.toResponseDtoList(roleRepository.findActiveRoles());
    }
}