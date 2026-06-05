package upc.ecovolt.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import upc.ecovolt.entity.Home;
import upc.ecovolt.mapping.dto.homedto.HomeMapper;
import upc.ecovolt.mapping.dto.homedto.HomeRequestDto;
import upc.ecovolt.mapping.dto.homedto.HomeResponseDto;
import upc.ecovolt.repository.DataCatalogRepository;
import upc.ecovolt.repository.HomeRepository;
import upc.ecovolt.repository.UserRepository;
import upc.ecovolt.security.UsuarioPrincipal;
import upc.ecovolt.service.HomeService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class HomeServiceImpl implements HomeService {

    private final HomeRepository homeRepository;
    private final UserRepository userRepository;
    private final DataCatalogRepository dataCatalogoRepository;
    private final HomeMapper homeMapper;

    /**
     * MÉTODO DE CIBERSEGURIDAD: Valida si el usuario actual es dueño de la propiedad.
     * Si es ROLE_ADMIN, se salta la validación.
     */
    private void validateOwnership(Long homeId) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var principal = (UsuarioPrincipal) authentication.getPrincipal();

        // Verificamos si NO es administrador
        boolean isAdmin = principal.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            Home home = homeRepository.findById(homeId)
                    .orElseThrow(() -> new RuntimeException("Error: Vivienda no encontrada."));

            if (!home.getUser().getId().equals(principal.getIdUser())) {
                log.error("VIOLACIÓN DE SEGURIDAD: El usuario {} intentó acceder a datos ajenos (Home ID: {})",
                        principal.getLogin(), homeId);
                throw new RuntimeException("Acceso denegado: No tienes permisos sobre esta propiedad.");
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<HomeResponseDto> findAllHomes() {
        // @PreAuthorize en la interfaz asegura que solo el Staff (Admin/Analyst) llegue aquí
        return homeMapper.toResponseDtoList(homeRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<HomeResponseDto> findHomeById(Long id) {
        validateOwnership(id); // Candado de propiedad
        return homeRepository.findById(id).map(homeMapper::toResponseDto);
    }

    @Override
    @Transactional
    public HomeResponseDto saveHome(HomeRequestDto requestDto) {
        var principal = (UsuarioPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // REGLA DE SEGURIDAD: Un cliente no puede registrar casas para otros IDs de usuario
        boolean isAdmin = principal.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin && !requestDto.getUserId().equals(principal.getIdUser())) {
            throw new RuntimeException("Error de seguridad: No puedes registrar viviendas para otros usuarios.");
        }

        var user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));

        var propertyType = dataCatalogoRepository.findById(requestDto.getPropertyTypeId())
                .orElseThrow(() -> new RuntimeException("Tipo de propiedad no válido."));

        log.info("USER {}: Registrando propiedad '{}'", principal.getLogin(), requestDto.getAlias());

        Home entity = homeMapper.toEntity(requestDto);
        entity.setUser(user);
        entity.setPropertyType(propertyType);
        entity.setUsuarioRegistro(principal.getLogin()); // Auditoría con el login del token

        return homeMapper.toResponseDto(homeRepository.save(entity));
    }

    @Override
    @Transactional
    public HomeResponseDto updateHome(Long id, HomeRequestDto requestDto) {
        validateOwnership(id); // Solo el dueño o admin puede editar

        return homeRepository.findById(id).map(existingHome -> {
            existingHome.setAddress(requestDto.getAddress());
            existingHome.setCity(requestDto.getCity());
            existingHome.setAlias(requestDto.getAlias());
            existingHome.setEnergyTariff(requestDto.getEnergyTariff());
            existingHome.setSquareMeters(requestDto.getSquareMeters().intValue());

            // Resolución de categoría si cambia
            var type = dataCatalogoRepository.findById(requestDto.getPropertyTypeId())
                    .orElseThrow(() -> new RuntimeException("Tipo no encontrado"));
            existingHome.setPropertyType(type);

            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            existingHome.setUsuarioActualizacion(username);

            return homeMapper.toResponseDto(homeRepository.save(existingHome));
        }).orElseThrow(() -> new RuntimeException("Vivienda no encontrada"));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        validateOwnership(id); // Solo el dueño puede eliminar
        log.warn("Eliminando propiedad ID: {}", id);
        homeRepository.deleteById(id);
    }

    // --- IMPLEMENTACIÓN DE MÉTODOS DE NEGOCIO ---

    @Override
    @Transactional(readOnly = true)
    public List<HomeResponseDto> findActiveHomesByUser(Long idUser) {
        // La protección de que idUser sea el mismo del token está en la Interfaz (@PreAuthorize)
        var homes = homeRepository.findActiveHomesByUser(idUser);
        return homeMapper.toResponseDtoList(homes);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HomeResponseDto> findByPropertyTypeName(String propertyTypeDescription) {
        return homeMapper.toResponseDtoList(homeRepository.findByPropertyTypeName(propertyTypeDescription));
    }

    @Override
    @Transactional(readOnly = true)
    public List<HomeResponseDto> findHomesByHighTariff(BigDecimal tariffThreshold) {
        return homeMapper.toResponseDtoList(homeRepository.findHomesByHighTariff(tariffThreshold));
    }

    @Override
    @Transactional(readOnly = true)
    public long countTotalDevicesByHome(Long idHome) {
        validateOwnership(idHome); // No puedes auditar inventario de otros
        return homeRepository.countTotalDevicesByHome(idHome);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HomeResponseDto> findByAliasAndUserId(String alias, Long idUser) {
        return homeMapper.toResponseDtoList(homeRepository.findByAliasAndUserId(alias, idUser));
    }

    @Override
    @Transactional(readOnly = true)
    public List<HomeResponseDto> findByCity(String city) {
        return homeMapper.toResponseDtoList(homeRepository.findByCity(city));
    }
}