package upc.ecovolt.repository;

import java.util.List;
import java.math.BigDecimal;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import upc.ecovolt.entity.Home;

public interface HomeRepository extends JpaRepository<Home, Long> {

    @Query("select h from Home h where " +
            "h.user.idUser = ?1 and " +
            "LOWER(h.alias) like ?2 and " +
            "(?3 = '' or h.city = ?3) and " +
            "(?4 = -1 or h.propertyType.idDataCatalog = ?4) and " +
            "h.status = 1")
    List<Home> consultaDinamica(Long idUser, String alias, String city, int idTipo);

    /*
     * REGLA DE NEGOCIO: Seguridad y Propiedad de Datos.
     * Carga el selector de casas en el Dashboard del usuario logueado.
     * Ordenado por alias para que el Frontend muestre una lista organizada.
     */
    @Query("select h from Home h where h.user.idUser = ?1 and h.status = 1 order by h.alias asc")
    List<Home> findActiveHomesByUser(Long idUser);

    /*
     * REGLA DE NEGOCIO: Segmentación por Tipo de Propiedad.
     * Uso de diccionarios (DataCatalog) para filtrar (Ej: 'Departamento').
     */
    @Query("select h from Home h where h.propertyType.description = ?1 and h.status = 1")
    List<Home> findByPropertyTypeName(String propertyTypeDescription);

    /*
     * REGLA DE NEGOCIO: Inteligencia Comercial / Tarifaria.
     * Busca hogares con tarifas altas para sugerencias de ahorro.
     */
    @Query("select h from Home h where h.energyTariff > ?1 and h.status = 1")
    List<Home> findHomesByHighTariff(BigDecimal tariffThreshold);

    /*
     * REGLA DE NEGOCIO: Auditoría de Inventario.
     * Cuenta dispositivos navegando Home -> Room -> Device.
     */
    @Query("select count(d) from Device d where d.room.home.idHome = ?1 and d.status = 1")
    long countTotalDevicesByHome(Long idHome);

    /*
     * REGLA DE NEGOCIO: Gestión Multi-propiedad.
     * Busca una propiedad específica por alias para un usuario.
     * Se usa Optional para manejar de forma segura si la casa no existe.
     */
    @Query("select h from Home h where h.alias = ?1 and h.user.idUser = ?2 and h.status = 1")
    Optional<Home> findByAliasAndUserId(String alias, Long idUser);

    /*
     * REGLA DE NEGOCIO: Búsqueda Geográfica.
     * Lista propiedades en una ciudad específica.
     */
    @Query("select h from Home h where h.city = ?1 and h.status = 1")
    List<Home> findByCity(String city);

    /*
     * REGLA DE NEGOCIO: Validación de Duplicidad.
     * Útil para el Frontend: evitar que un usuario registre dos casas con el mismo alias.
     */
    @Query("select case when count(h) > 0 then true else false end from Home h where h.alias = ?1 and h.user.idUser = ?2 and h.status = 1")
    boolean existsByAliasAndUserId(String alias, Long idUser);
}