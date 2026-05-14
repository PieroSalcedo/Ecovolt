package upc.ecovolt.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import upc.ecovolt.entity.Option;

@Repository
public interface OptionRepository extends JpaRepository<Option, Integer> {

    /*
     * REGLA DE NEGOCIO: Construcción Dinámica de Interfaz.
     * Filtra opciones según su tipo (Ej: 1 para Menús laterales, 2 para Botones).
     * Esto permite que el Frontend cargue solo lo necesario para la navegación.
     */
    @Query("select o from Option o where o.tipo = ?1 and o.estado = 1")
    List<Option> findByType(Integer tipo);

    /*
     * REGLA DE NEGOCIO: Disponibilidad de Funciones.
     * Lista todas las opciones operativas del ecosistema Ecovolt.
     */
    @Query("select o from Option o where o.estado = 1")
    List<Option> findActiveOptions();

    /*
     * REGLA DE NEGOCIO: Validación de Rutas.
     * Busca una opción por su ruta (Ej: '/dashboard/analytics').
     * Sirve para verificar permisos de navegación en el Backend.
     */
    @Query("select o from Option o where o.ruta = ?1")
    List<Option> findByRuta(String ruta);
}