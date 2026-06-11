package upc.ecovolt.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import upc.ecovolt.entity.Option;
import upc.ecovolt.mapping.dto.OptionDto;
import upc.ecovolt.mapping.dto.OptionMapper;
import upc.ecovolt.repository.OptionRepository;
import upc.ecovolt.service.OptionService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OptionServiceImpl implements OptionService {

    private final OptionRepository optionRepository;
    private final OptionMapper optionMapper;

    @Override
    @Transactional(readOnly = true)
    public List<OptionDto.Response> findAll() {
        return optionMapper.toResponseDtoList(optionRepository.findAll());
    }

    @Override
    @Transactional
    public OptionDto.Response save(OptionDto.Request requestDto) {
        // 1. REGLA DE NEGOCIO: Normalización de rutas de navegación
        // Tu DTO usa 'url' pero tu entidad usa 'route'. El Mapper se encarga de esto.
        // Aquí normalizamos el String para asegurar que siempre empiece con '/'
        String route = requestDto.getUrl().trim();
        if (!route.startsWith("/")) {
            route = "/" + route;
        }

        // 2. VALIDACIÓN: Evitar colisión de rutas (deben ser únicas)
        if (optionRepository.findByRoute(route).isPresent()) {
            log.error("CONFIG ERROR: Ya existe una opción con la ruta {}", route);
            throw new RuntimeException("La ruta de navegación '" + route + "' ya está asignada.");
        }

        log.info("SISTEMA: Registrando nueva opción de menú: {}", requestDto.getName());

        Option entity = optionMapper.toEntity(requestDto);
        entity.setRoute(route); // Aseguramos la ruta normalizada
        entity.setStatus(1);    // Activa por defecto

        return optionMapper.toResponseDto(optionRepository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public List<OptionDto.Response> findByType(Integer type) {
        // Filtra por tipo (1: Menú Sidebar, 2: Botón Acción, etc.)
        return optionMapper.toResponseDtoList(optionRepository.findByType(type));
    }

    @Override
    @Transactional(readOnly = true)
    public List<OptionDto.Response> findActiveOptions() {
        return optionMapper.toResponseDtoList(optionRepository.findActiveOptions());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OptionDto.Response> findOptionsByRoleId(Integer idRole) {
        log.debug("Cargando permisos dinámicos para el Rol ID: {}", idRole);
        var options = optionRepository.findOptionsByRoleId(idRole);
        return optionMapper.toResponseDtoList(options);
    }
}