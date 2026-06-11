package upc.ecovolt.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import upc.ecovolt.entity.DataCatalog;
import upc.ecovolt.mapping.dto.DataCatalogDto;
import upc.ecovolt.mapping.dto.DataCatalogMapper;
import upc.ecovolt.repository.CatalogRepository;
import upc.ecovolt.repository.DataCatalogRepository;
import upc.ecovolt.service.DataCatalogService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataCatalogServiceImpl implements DataCatalogService {

    private final DataCatalogRepository dataCatalogRepository;
    private final CatalogRepository catalogRepository;
    private final DataCatalogMapper dataCatalogMapper;

    @Override
    @Transactional(readOnly = true)
    public List<DataCatalogDto.Response> findAll() {
        log.debug("Listando todos los ítems de datos del catálogo");
        return dataCatalogMapper.toResponseDtoList(dataCatalogRepository.findAll());
    }

    @Override
    @Transactional
    public DataCatalogDto.Response save(DataCatalogDto.Request requestDto) {
        log.info("Registrando nueva opción '{}' para el catálogo ID: {}",
                requestDto.getValue(), requestDto.getIdCatalog());

        // 1. REGLA DE INTEGRIDAD: Validar existencia del catálogo maestro
        var masterCatalog = catalogRepository.findById(requestDto.getIdCatalog().intValue())
                .orElseThrow(() -> new RuntimeException("Error: El catálogo maestro ID " + requestDto.getIdCatalog() + " no existe."));

        // 2. Mapear DTO a Entidad
        DataCatalog entity = dataCatalogMapper.toEntity(requestDto);

        // 3. REGLA TÉCNICA: Establecer relación bidireccional manual
        // (Asegura que la FK id_catalog se inserte correctamente)
        entity.setCatalog(masterCatalog);
        entity.setStatus(1); // Por defecto activo al crear

        DataCatalog savedEntity = dataCatalogRepository.save(entity);
        return dataCatalogMapper.toResponseDto(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DataCatalogDto.Response> findByCatalogDescription(String catalogDescription) {
        log.debug("Cargando diccionario para el Frontend: {}", catalogDescription);

        // Usamos el método mejorado que devuelve los datos ordenados alfabéticamente
        var data = dataCatalogRepository.findByCatalog_DescriptionOrderByDescriptionAsc(catalogDescription);
        return dataCatalogMapper.toResponseDtoList(data);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DataCatalogDto.Response> findByDescriptionAndCatalog(String description, String catalogDescription) {
        // En el repositorio mejoramos esto para que devuelva List u Optional
        var data = dataCatalogRepository.findByDescriptionAndCatalog_Description(description, catalogDescription);

        // Convertimos el resultado (si es uno solo) a lista para cumplir con el contrato de la interfaz
        return data.map(dc -> List.of(dataCatalogMapper.toResponseDto(dc)))
                .orElse(List.of());
    }
}