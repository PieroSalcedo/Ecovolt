package upc.ecovolt.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import upc.ecovolt.entity.Home;
import upc.ecovolt.mapping.dto.homedto.HomeMapper;
import upc.ecovolt.mapping.dto.homedto.HomeRequestDto;
import upc.ecovolt.mapping.dto.homedto.HomeResponseDto;
import upc.ecovolt.repository.DataCatalogoRepository;
import upc.ecovolt.repository.HomeRepository;
import upc.ecovolt.repository.UserRepository;
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
    private final DataCatalogoRepository dataCatalogoRepository; // Para resolver el tipo de propiedad
    private final HomeMapper homeMapper;

    @Override
    @Transactional(readOnly = true)
    public List<HomeResponseDto> findAllHomes() {
        return homeMapper.toResponseDtoList(homeRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<HomeResponseDto> findHomeById(Long id) {
        return homeRepository.findById(id).map(homeMapper::toResponseDto);
    }

    @Override
    @Transactional
    public HomeResponseDto saveHome(HomeRequestDto requestDto) {
        // 1. REGLA DE NEGOCIO: Validar existencia del dueño
        var user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new RuntimeException("Error: El Usuario dueño no existe."));

        // 2. REGLA TÉCNICA: Resolver el Tipo de Propiedad desde DataCatalogo
        var propertyType = dataCatalogoRepository.findById(requestDto.getPropertyTypeId())
                .orElseThrow(() -> new RuntimeException("Error: El Tipo de Propiedad (Catálogo) no existe."));

        log.info("Registrando vivienda '{}' para el usuario: {}", requestDto.getAlias(), user.getLogin());

        Home entity = homeMapper.toEntity(requestDto);
        entity.setUser(user);
        entity.setPropertyType(propertyType); // Seteamos el objeto completo del catálogo

        return homeMapper.toResponseDto(homeRepository.save(entity));
    }

    @Override
    @Transactional
    public HomeResponseDto updateHome(Long id, HomeRequestDto requestDto) {
        return homeRepository.findById(id).map(existingHome -> {
            existingHome.setAddress(requestDto.getAddress());
            existingHome.setCity(requestDto.getCity());
            existingHome.setAlias(requestDto.getAlias());
            existingHome.setEnergyTariff(requestDto.getEnergyTariff());
            existingHome.setSquareMeters(requestDto.getSquareMeters());

            // Actualizar el tipo de propiedad si cambió
            var propertyType = dataCatalogoRepository.findById(requestDto.getPropertyTypeId())
                    .orElseThrow(() -> new RuntimeException("Tipo de Propiedad no encontrado"));
            existingHome.setPropertyType(propertyType);

            return homeMapper.toResponseDto(homeRepository.save(existingHome));
        }).orElseThrow(() -> new RuntimeException("Vivienda no encontrada con ID: " + id));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!homeRepository.existsById(id)) throw new RuntimeException("La vivienda no existe.");
        homeRepository.deleteById(id);
    }

    // --- IMPLEMENTACIÓN DE MÉTODOS DE NEGOCIO ---

    @Override
    @Transactional(readOnly = true)
    public List<HomeResponseDto> findActiveHomesByUser(Long idUser) {
        var homes = homeRepository.findActiveHomesByUser(idUser);
        return homeMapper.toResponseDtoList(homes);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HomeResponseDto> findByPropertyTypeName(String propertyTypeDescription) {
        // Ejemplo: "Departamento", "Casa", etc.
        var homes = homeRepository.findByPropertyTypeName(propertyTypeDescription);
        return homeMapper.toResponseDtoList(homes);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HomeResponseDto> findHomesByHighTariff(BigDecimal tariffThreshold) {
        log.info("Buscando hogares con tarifa superior a: {}", tariffThreshold);
        var homes = homeRepository.findHomesByHighTariff(tariffThreshold);
        return homeMapper.toResponseDtoList(homes);
    }

    @Override
    @Transactional(readOnly = true)
    public long countTotalDevicesByHome(Long idHome) {
        // REGLA DE NEGOCIO: Auditoría de inventario total
        return homeRepository.countTotalDevicesByHome(idHome);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HomeResponseDto> findByAliasAndUserId(String alias, Long idUser) {
        var homes = homeRepository.findByAliasAndUserId(alias, idUser);
        return homeMapper.toResponseDtoList(homes);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HomeResponseDto> findByCity(String city) {
        var homes = homeRepository.findByCity(city);
        return homeMapper.toResponseDtoList(homes);
    }
}