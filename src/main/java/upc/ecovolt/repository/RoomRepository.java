package upc.ecovolt.repository;

import java.util.List;
import java.math.BigDecimal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import upc.ecovolt.entity.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    /*
     * REGLA DE NEGOCIO: Integridad de Estructura.
     * Lista todas las habitaciones activas de una propiedad específica.
     */
    @Query("select r from Room r where r.home.idHome = ?1 and r.status = 1")
    List<Room> findByHomeId(Long idHome);

    /*
     * REGLA DE NEGOCIO: Análisis por Tipo de Ambiente (DataCatalogo).
     * Permite agrupar ambientes similares (Ej: Todas las 'Cocinas') para
     * realizar comparativas de consumo promedio por tipo de habitación.
     */
    @Query("select r from Room r where r.roomType.description = ?1 and r.status = 1")
    List<Room> findByRoomTypeName(String typeDescription);

    /*
     * REGLA DE NEGOCIO: Cálculo de Densidad Energética.
     * Busca habitaciones que superen un área determinada. Útil para identificar
     * espacios grandes que requieren mayor iluminación o climatización.
     */
    @Query("select r from Room r where r.areaSqm > ?1 and r.status = 1")
    List<Room> findLargeRooms(BigDecimal minArea);

    /*
     * REGLA DE NEGOCIO: Auditoría de Dispositivos por Ambiente.
     * Cuenta cuántos dispositivos IoT están instalados en una habitación específica.
     * Cruce: Device -> Room.
     */
    @Query("select count(d) from Device d where d.room.idRoom = ?1")
    long countDevicesInRoom(Long idRoom);

    /*
     * REGLA DE NEGOCIO: Ubicación Vertical.
     * Lista habitaciones en un piso específico (Ej: Piso 2).
     */
    @Query("select r from Room r where r.home.idHome = ?1 and r.floorNumber = ?2")
    List<Room> findByHomeAndFloor(Long idHome, Integer floor);

    /*
     * REGLA DE NEGOCIO: Búsqueda por Nombre.
     * Busca una habitación específica por su nombre dentro de una casa.
     */
    @Query("select r from Room r where r.name = ?1 and r.home.idHome = ?2")
    List<Room> findByNameAndHome(String name, Long idHome);
}