package upc.ecovolt.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import upc.ecovolt.entity.DataCatalogo;

@Repository
public interface DataCatalogoRepository extends JpaRepository<DataCatalogo, Integer> {

    /*
     * REGLA DE NEGOCIO: Carga de Diccionarios (Estilo SUNAT).
     * Obtiene todos los ítems de una categoría usando el nombre del catálogo.
     * Ejemplo: "Tráeme todo lo que pertenezca a 'ROOM_TYPES'".
     */
    @Query("select d from DataCatalogo d where d.catalogo.description = ?1")
    List<DataCatalogo> findByCatalogDescription(String catalogDescription);

    /*
     * REGLA DE NEGOCIO: Búsqueda exacta.
     * Busca un ítem específico (Ej: 'Kitchen') dentro de un catálogo.
     */
    @Query("select d from DataCatalogo d where d.description = ?1 and d.catalogo.description = ?2")
    List<DataCatalogo> findByDescriptionAndCatalog(String description, String catalogDescription);
}