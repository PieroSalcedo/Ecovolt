package upc.ecovolt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import upc.ecovolt.entity.Catalog;
import java.util.List;
import java.util.Optional;

public interface CatalogRepository extends JpaRepository<Catalog, Integer> {
    /*
     * REGLA DE NEGOCIO: Búsqueda por descripción exacta.
     * Al ser un método de consulta derivado, Spring lo resuelve automáticamente
     * sin necesidad de escribir el @Query de forma manual.
     */
    Optional<Catalog> findByDescription(String description);

    /*
     * REGLA DE NEGOCIO: Búsqueda dinámica para el Frontend.
     * Permite filtrar el catálogo por nombre mientras el usuario escribe,
     * ignorando mayúsculas y minúsculas (ideal para tablas con buscadores).
     */
    List<Catalog> findByDescriptionContainingIgnoreCase(String description);

    /*
     * REGLA DE NEGOCIO: Validación de Duplicidad.
     * Antes de guardar un nuevo catálogo, verificamos si ya existe la descripción.
     */
    boolean existsByDescription(String description);
}