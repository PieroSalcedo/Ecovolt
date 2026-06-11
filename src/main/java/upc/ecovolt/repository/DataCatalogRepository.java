package upc.ecovolt.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import upc.ecovolt.entity.DataCatalog;

public interface DataCatalogRepository extends JpaRepository<DataCatalog, Integer> {

    /*
     * REGLA DE NEGOCIO: Carga de Selects/Combos para el Frontend.
     * Obtiene los valores de un catálogo específico ordenados alfabéticamente.
     * Ejemplo: "Traer todas las habitaciones (SALA, COCINA)" filtrando por 'TIPO_HABITACION'.
     */
    List<DataCatalog> findByCatalog_DescriptionOrderByDescriptionAsc(String catalogDescription);

    /*
     * REGLA DE NEGOCIO: Filtrado por ID de Catálogo.
     * Muy útil para el Frontend cuando ya se tiene el ID de la categoría maestra.
     */
    List<DataCatalog> findByCatalog_IdCatalog(Integer idCatalog);

    /*
     * REGLA DE NEGOCIO: Búsqueda de valor específico dentro de un grupo.
     * Se usa para validar si un ítem (ej. 'Dormitorio') ya existe dentro de un catálogo.
     */
    Optional<DataCatalog> findByDescriptionAndCatalog_Description(String description, String catalogDescription);

    /*
     * REGLA DE NEGOCIO: Buscador dinámico (Autocomplete).
     * Permite al usuario buscar opciones por nombre ignorando mayúsculas/minúsculas.
     */
    List<DataCatalog> findByDescriptionContainingIgnoreCase(String description);
}