package upc.ecovolt.repository;

import java.util.List;
import java.math.BigDecimal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import upc.ecovolt.entity.Home;

@Repository
public interface HomeRepository extends JpaRepository<Home, Long> {

    /*
     * REGLA DE NEGOCIO: Seguridad y Propiedad de Datos.
     * Lista las propiedades activas que pertenecen al usuario logueado.
     * Se usa para cargar el selector de casas en el Dashboard.
     */
    @Query("select h from Home h where h.user.idUser = ?1 and h.status = 1")
    List<Home> findActiveHomesByUser(Long idUser);

    /*
     * REGLA DE NEGOCIO: Segmentación por Tipo de Propiedad.
     * Filtra hogares usando la descripción del DataCatalogo (Ej: 'Departamento', 'Casa').
     * Demuestra el uso correcto de los diccionarios implementados.
     */
    @Query("select h from Home h where h.propertyType.description = ?1 and h.status = 1")
    List<Home> findByPropertyTypeName(String propertyTypeDescription);

    /*
     * REGLA DE NEGOCIO: Inteligencia Comercial / Tarifaria.
     * Identifica hogares con tarifas eléctricas superiores a un umbral
     * para enviar sugerencias automáticas de ahorro.
     */
    @Query("select h from Home h where h.energyTariff > ?1 and h.status = 1")
    List<Home> findHomesByHighTariff(BigDecimal tariffThreshold);

    /*
     * REGLA DE NEGOCIO: Auditoría de Inventario (INNER JOIN implícito).
     * Cuenta el total de dispositivos instalados en toda la casa navegando por:
     * Home -> Room -> Device.
     */
    @Query("select count(d) from Device d where d.room.home.idHome = ?1")
    long countTotalDevicesByHome(Long idHome);

    /*
     * REGLA DE NEGOCIO: Gestión Multi-propiedad.
     * Busca una propiedad específica por su alias (Ej: 'Oficina') para un usuario.
     */
    @Query("select h from Home h where h.alias = ?1 and h.user.idUser = ?2")
    List<Home> findByAliasAndUserId(String alias, Long idUser);

    /*
     * REGLA DE NEGOCIO: Análisis por Ciudad.
     * Lista propiedades en una ubicación específica para reportes regionales.
     */
    @Query("select h from Home h where h.city = ?1 and h.status = 1")
    List<Home> findByCity(String city);
}