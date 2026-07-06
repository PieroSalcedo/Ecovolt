package upc.ecovolt.service;

import upc.ecovolt.mapping.dto.DataCatalogDto;
import java.util.List;

public interface DataCatalogService {

    List<DataCatalogDto.Response> findAll();

    /*
     * REGLA DE NEGOCIO: Cargar combos dinámicos por nombre de categoría maestra.
     * Ejemplo: "TIPO_HABITACION", "ESTADO_DISPOSITIVO".
     */
    List<DataCatalogDto.Response> findByCatalogDescription(String catalogDescription);

    /*
     * REGLA DE NEGOCIO: Buscar una opción específica dentro de un grupo.
     */
    List<DataCatalogDto.Response> findByDescriptionAndCatalog(String description, String catalogDescription);

    DataCatalogDto.Response save(DataCatalogDto.Request requestDto);
}