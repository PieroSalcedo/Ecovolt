package upc.ecovolt.repository;

import java.util.List;
import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import upc.ecovolt.entity.Room;

public interface RoomRepository extends JpaRepository<Room, Long> {

    @Query("select r from Room r where " +
            "(:idHome = -1 or r.home.idHome = :idHome) and " +
            "LOWER(r.name) LIKE LOWER(CONCAT('%', :name, '%')) and " +
            "(:idTipo = -1 or r.roomType.idDataCatalog = :idTipo) and " +
            "r.status = 1")
    List<Room> consultaCuartoDinamica(
            @Param("idHome") Long idHome,
            @Param("name") String name,
            @Param("idTipo") int idTipo);
    /*
     * REGLA DE NEGOCIO: Integridad de Estructura.
     * Lista todas las habitaciones activas de una propiedad.
     * Ordenado por nombre para facilitar la navegación en el Dashboard.
     */
    @Query("select r from Room r where r.home.idHome = ?1 and r.status = 1 order by r.name asc")
    List<Room> findByHomeId(Long idHome);

    /*
     * REGLA DE NEGOCIO: Análisis por Tipo de Ambiente (DataCatalog).
     * Ejemplo: "Listar todos los Dormitorios del sistema".
     */
    @Query("select r from Room r where r.roomType.description = ?1 and r.status = 1")
    List<Room> findByRoomTypeName(String typeDescription);

    /*
     * REGLA DE NEGOCIO: Cálculo de Densidad Energética.
     * Identifica espacios grandes que podrían requerir planes de ahorro específicos.
     */
    @Query("select r from Room r where r.areaSqm > ?1 and r.status = 1")
    List<Room> findLargeRooms(BigDecimal minArea);

    /*
     * REGLA DE NEGOCIO: Auditoría de Dispositivos por Ambiente.
     * Cuenta solo los dispositivos ACTIVOS instalados en una habitación.
     */
    @Query("select count(d) from Device d where d.room.idRoom = ?1 and d.status = 1")
    long countDevicesInRoom(Long idRoom);

    /*
     * REGLA DE NEGOCIO: Ubicación Vertical.
     * Filtra ambientes por piso y propiedad. Útil para mapas de calor por niveles.
     */
    @Query("select r from Room r where r.home.idHome = ?1 and r.floorNumber = ?2 and r.status = 1")
    List<Room> findByHomeAndFloor(Long idHome, Integer floor);

    /*
     * REGLA DE NEGOCIO: Búsqueda por Nombre.
     * Busca una habitación específica por nombre dentro de una casa.
     * Se usa Optional para una gestión de errores más limpia en el Service.
     */
    @Query("select r from Room r where r.name = ?1 and r.home.idHome = ?2 and r.status = 1")
    Optional<Room> findByNameAndHome(String name, Long idHome);

    /*
     * REGLA DE NEGOCIO: Validación de Duplicidad.
     * Evita que un usuario cree dos "Cocinas" en la misma casa.
     */
    @Query("select case when count(r) > 0 then true else false end from Room r where r.name = ?1 and r.home.idHome = ?2 and r.status = 1")
    boolean existsByNameAndHome(String name, Long idHome);
}