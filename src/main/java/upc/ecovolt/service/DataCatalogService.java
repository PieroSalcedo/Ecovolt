package upc.ecovolt.service;

import upc.ecovolt.mapping.dto.DataCatalogDto;
import java.util.List;

public interface DataCatalogService {

    List<DataCatalogDto> findAll();

    List<DataCatalogDto> findByCatalogDescription(String catalogDescription);

    List<DataCatalogDto> findByDescriptionAndCatalog(String description, String catalogDescription);

    DataCatalogDto save(DataCatalogDto requestDto);
}