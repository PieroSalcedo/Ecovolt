package upc.ecovolt.service;

import upc.ecovolt.mapping.dto.datacatalogodto.DataCatalogoRequestDto;
import upc.ecovolt.mapping.dto.datacatalogodto.DataCatalogoResponseDto;
import java.util.List;

public interface DataCatalogoService {
    List<DataCatalogoResponseDto> findAll();
    DataCatalogoResponseDto save(DataCatalogoRequestDto requestDto);

    // REGLA DE NEGOCIO (Provenientes del Repositorio)
    List<DataCatalogoResponseDto> findByCatalogDescription(String catalogDescription);
    List<DataCatalogoResponseDto> findByDescriptionAndCatalog(String description, String catalogDescription);
}