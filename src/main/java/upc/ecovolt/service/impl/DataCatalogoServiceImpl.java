package upc.ecovolt.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import upc.ecovolt.entity.DataCatalogo;
import upc.ecovolt.mapping.dto.datacatalogodto.DataCatalogoMapper;
import upc.ecovolt.mapping.dto.datacatalogodto.DataCatalogoRequestDto;
import upc.ecovolt.mapping.dto.datacatalogodto.DataCatalogoResponseDto;
import upc.ecovolt.repository.CatalogoRepository; // IMPORTANTE: Inyectar el repo maestro
import upc.ecovolt.repository.DataCatalogoRepository;
import upc.ecovolt.service.DataCatalogoService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataCatalogoServiceImpl implements DataCatalogoService {

    private final DataCatalogoRepository dataCatalogoRepository;
    private final CatalogoRepository catalogoRepository; // Para validar que la "caja" existe
    private final DataCatalogoMapper dataCatalogoMapper;

    @Override
    @Transactional(readOnly = true)
    public List<DataCatalogoResponseDto> findAll() {
        var entities = dataCatalogoRepository.findAll();
        return dataCatalogoMapper.toResponseDtoList(entities);
    }

    @Override
    @Transactional
    public DataCatalogoResponseDto save(DataCatalogoRequestDto requestDto) {
        log.info("Añadiendo nueva opción '{}' al catálogo maestro ID: {}",
                requestDto.getDescription(), requestDto.getIdCatalog());

        // 1. REGLA DE INTEGRIDAD: Validar que el catálogo maestro exista
        var masterCatalog = catalogoRepository.findById(requestDto.getIdCatalog())
                .orElseThrow(() -> new RuntimeException("Error: El catálogo maestro no existe."));

        // 2. Mapear DTO a Entidad
        DataCatalogo entity = dataCatalogoMapper.toEntity(requestDto);

        // 3. REGLA TÉCNICA: Asignar el objeto maestro completo
        // MapStruct no puede adivinar el objeto maestro solo con el ID, lo hacemos manual aquí.
        entity.setCatalogo(masterCatalog);

        return dataCatalogoMapper.toResponseDto(dataCatalogoRepository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DataCatalogoResponseDto> findByCatalogDescription(String catalogDescription) {
        // REGLA DE NEGOCIO: Cargar combos dinámicos (Ej: "ROOM_TYPES", "DEVICE_CATEGORIES")
        log.debug("Buscando opciones para el diccionario: {}", catalogDescription);
        var data = dataCatalogoRepository.findByCatalogDescription(catalogDescription);
        return dataCatalogoMapper.toResponseDtoList(data);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DataCatalogoResponseDto> findByDescriptionAndCatalog(String description, String catalogDescription) {
        var data = dataCatalogoRepository.findByDescriptionAndCatalog(description, catalogDescription);
        return dataCatalogoMapper.toResponseDtoList(data);
    }
}