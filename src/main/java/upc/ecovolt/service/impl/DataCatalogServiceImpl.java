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
        var entities = dataCatalogRepository.findAll();
        return dataCatalogMapper.toResponseDtoList(entities);
    }

    @Override
    @Transactional
    public DataCatalogDto.Response save(DataCatalogDto.Request requestDto) {
        log.info("Anadiendo nueva opcion '{}' al catalogo maestro ID: {}",
                requestDto.getDescription(), requestDto.getIdCatalog());

        var masterCatalog = catalogRepository.findById(requestDto.getIdCatalog().intValue())
                .orElseThrow(() -> new RuntimeException("Error: El catalogo maestro no existe."));

        DataCatalog entity = dataCatalogMapper.toEntity(requestDto);
        entity.setCatalog(masterCatalog);

        return dataCatalogMapper.toResponseDto(dataCatalogRepository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DataCatalogDto.Response> findByCatalogDescription(String catalogDescription) {
        log.debug("Buscando opciones para el diccionario: {}", catalogDescription);
        var data = dataCatalogRepository.findByCatalogDescription(catalogDescription);
        return dataCatalogMapper.toResponseDtoList(data);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DataCatalogDto.Response> findByDescriptionAndCatalog(String description, String catalogDescription) {
        var data = dataCatalogRepository.findByDescriptionAndCatalog(description, catalogDescription);
        return dataCatalogMapper.toResponseDtoList(data);
    }
}
