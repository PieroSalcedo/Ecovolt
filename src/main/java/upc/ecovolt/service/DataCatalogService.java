package upc.ecovolt.service;

import upc.ecovolt.mapping.dto.DataCatalogDto;
import java.util.List;

public interface DataCatalogService {

    List<DataCatalogDto.Response> findAll();

    List<DataCatalogDto.Response> findByCatalogDescription(String catalogDescription);

    List<DataCatalogDto.Response> findByDescriptionAndCatalog(String description, String catalogDescription);

    DataCatalogDto.Response save(DataCatalogDto.Request requestDto);
}
