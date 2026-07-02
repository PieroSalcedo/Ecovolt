package upc.ecovolt.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import upc.ecovolt.entity.DataCatalog;
import upc.ecovolt.entity.Home;
import upc.ecovolt.mapping.dto.HomeDto;
import upc.ecovolt.mapping.dto.HomeMapper;
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
    private final DataCatalogRepository dataCatalogRepository;
    private final HomeMapper homeMapper;

    /**
     * CIBERSEGURIDAD: Valida si el usuario actual tiene derechos sobre la propiedad.
     */
    private void validateOwnership(Long idHome) {
        var principal = (UsuarioPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean isAdmin = principal.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            Home home = homeRepository.findById(idHome)
                    .orElseThrow(() -> new RuntimeException("Error: Vivienda no encontrada."));

            if (!home.getUser().getIdUser().equals(principal.getIdUser())) {
                log.error("VIOLACIÓN DE SEGURIDAD: Usuario {} intentó acceder a Home ID: {}", principal.getLogin(), idHome);
                throw new RuntimeException("Acceso denegado: No tienes permisos sobre esta propiedad.");
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<HomeDto.Response> consultaHomeDinamica(Long idUser, String alias, String city, int idTipo) {
        List<Home> lista = homeRepository.consultaHomeDinamica(idUser, alias, city, idTipo);
        return homeMapper.toResponseDtoList(lista);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HomeDto.Response> findAllHomes() {
        return homeMapper.toResponseDtoList(homeRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<HomeDto.Response> findHomeById(Long idHome) {
        validateOwnership(idHome);
        return homeRepository.findById(idHome).map(homeMapper::toResponseDto);
    }

    @Override
    @Transactional
    public HomeDto.Response saveHome(HomeDto.Request requestDto) {
        var principal = (UsuarioPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // REGLA DE SEGURIDAD: Un cliente no puede registrar casas para otros usuarios.
        boolean isAdmin = principal.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin && !requestDto.getIdUser().equals(principal.getIdUser())) {
            throw new RuntimeException("Error: No puedes registrar viviendas para otros usuarios.");
        }

        var user = userRepository.findById(requestDto.getIdUser())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));

        // Nota: En tu HomeDto.Request debes asegurarte de tener el campo idPropertyType
        // Si no existe, puedes pasarlo como parámetro o agregarlo al DTO.

        log.info("REGISTRO PROPIEDAD: '{}' para el usuario {}", requestDto.getAddress(), principal.getLogin());

        Home entity = homeMapper.toEntity(requestDto);
        entity.setUser(user);
        entity.setStatus(1); // Activa al crear

        return homeMapper.toResponseDto(homeRepository.save(entity));
    }

    @Override
    @Transactional
    public HomeDto.Response updateHome(Long idHome, HomeDto.Request requestDto) {
        return homeRepository.findById(idHome).map(existing -> {
            existing.setAddress(requestDto.getAddress());
            existing.setAlias(requestDto.getAlias());
            existing.setCity(requestDto.getCity());
            existing.setEnergyTariff(requestDto.getEnergyTariff());
            existing.setSquareMeters(requestDto.getSquareMeters());

            // Actualizar tipo de propiedad
            if (requestDto.getIdPropertyType() != null) {
                DataCatalog type = new DataCatalog();
                type.setIdDataCatalog(requestDto.getIdPropertyType());
                existing.setPropertyType(type);
            }

            return homeMapper.toResponseDto(homeRepository.save(existing));
        }).orElseThrow(() -> new RuntimeException("Vivienda no encontrada"));
    }

    @Override
    @Transactional
    public void delete(Long idHome) {
        validateOwnership(idHome);

        // REGLA DE NEGOCIO: Borrado lógico para no perder historial de telemetría
        homeRepository.findById(idHome).ifPresent(h -> {
            h.setStatus(0);
            homeRepository.save(h);
            log.warn("BORRADO LÓGICO: Vivienda ID {} marcada como inactiva", idHome);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public List<HomeDto.Response> findActiveHomesByUser(Long idUser) {
        return homeMapper.toResponseDtoList(homeRepository.findActiveHomesByUser(idUser));
    }

    @Override
    @Transactional(readOnly = true)
    public List<HomeDto.Response> findByPropertyTypeName(String propertyTypeDescription) {
        return homeMapper.toResponseDtoList(homeRepository.findByPropertyTypeName(propertyTypeDescription));
    }

    @Override
    @Transactional(readOnly = true)
    public List<HomeDto.Response> findHomesByHighTariff(BigDecimal tariffThreshold) {
        return homeMapper.toResponseDtoList(homeRepository.findHomesByHighTariff(tariffThreshold));
    }

    @Override
    @Transactional(readOnly = true)
    public long countTotalDevicesByHome(Long idHome) {
        validateOwnership(idHome);
        return homeRepository.countTotalDevicesByHome(idHome);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<HomeDto.Response> findByAliasAndUserId(String alias, Long idUser) {
        return homeRepository.findByAliasAndUserId(alias, idUser).map(homeMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HomeDto.Response> findByCity(String city) {
        return homeMapper.toResponseDtoList(homeRepository.findByCity(city));
    }
}