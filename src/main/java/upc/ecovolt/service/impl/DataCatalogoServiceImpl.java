package upc.ecovolt.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import upc.ecovolt.mapping.dto.datacatalogodto.DataCatalogoMapper;
import upc.ecovolt.mapping.dto.datacatalogodto.DataCatalogoRequestDto;
import upc.ecovolt.mapping.dto.datacatalogodto.DataCatalogoResponseDto;
import upc.ecovolt.repository.DataCatalogoRepository;
import upc.ecovolt.service.DataCatalogoService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DataCatalogoServiceImpl implements DataCatalogoService {

    private final DataCatalogoRepository dataCatalogoRepository;
    private final DataCatalogoMapper dataCatalogoMapper;

    @Override
    @Transactional(readOnly = true)
    public List<DataCatalogoResponseDto> findAll() {
        return dataCatalogoMapper.toResponseDtoList(dataCatalogoRepository.findAll());
    }

    @Override
    @Transactional
    public DataCatalogoResponseDto save(DataCatalogoRequestDto requestDto) {
        var entity = dataCatalogoMapper.toEntity(requestDto);
        return dataCatalogoMapper.toResponseDto(dataCatalogoRepository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DataCatalogoResponseDto> findByCatalogDescription(String catalogDescription) {
        // REGLA DE NEGOCIO: Cargar combos dinámicos (Ej: "ROOM_TYPES")
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