package upc.ecovolt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import upc.ecovolt.entity.Catalogo;
import java.util.Optional;

@Repository
public interface CatalogoRepository extends JpaRepository<Catalogo, Integer> {

    /*
     * REGLA DE NEGOCIO: Buscar categoría maestra por nombre.
     * Ejemplo: Buscar 'ROOM_TYPES' para verificar si el catálogo existe.
     */
    @Query("select c from Catalogo c where c.description = ?1")
    Optional<Catalogo> findByDescription(String description);
}