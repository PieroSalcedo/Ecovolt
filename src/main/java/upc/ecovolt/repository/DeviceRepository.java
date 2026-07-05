package upc.ecovolt.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import upc.ecovolt.entity.Device;

public interface DeviceRepository extends JpaRepository<Device, Long> {

    @Query("select d from Device d where " +
            "(:idHome = -1 or d.room.home.idHome = :idHome) and " +
            "(:idRoom = -1 or d.room.idRoom = :idRoom) and " +
            "LOWER(d.name) LIKE LOWER(CONCAT('%', :name, '%')) and " +
            "d.status = 1")
    List<Device> consultaDispositivoDinamica(
            @Param("idHome") Long idHome,
            @Param("idRoom") Long idRoom,
            @Param("name") String name);

    @Query("select d from Device d where d.room.home.user.idUser = ?1 and d.status = 1")
    List<Device> findByUserId(Long idUser);

    /*
     * REGLA DE NEGOCIO: Identificación Única de Hardware.
     * Busca un dispositivo por su número de serie (MAC/UUID).
     */
    Optional<Device> findBySerialNumber(String serialNumber);

    /*
     * REGLA DE NEGOCIO: Segmentación por Categoría.
     * Lista dispositivos activos (status=1) por el nombre de su categoría.
     * Ejemplo: "Iluminación", "Climatización".
     */
    List<Device> findByCategory_DescriptionAndStatus(String description, Integer status);

    /*
     * REGLA DE NEGOCIO: Inventario por Habitación.
     * Filtra equipos activos en una habitación específica.
     */
    List<Device> findByRoom_IdRoomAndStatus(Long idRoom, Integer status);

    /*
     * REGLA DE NEGOCIO: Inventario por Propiedad (Navegación Profunda).
     * El Frontend lo usará para listar todos los dispositivos de una casa.
     * Ruta: Device -> Room -> Home.
     */
    List<Device> findByRoom_Home_IdHomeAndStatus(Long idHome, Integer status);

    /*
     * REGLA DE NEGOCIO: Análisis de Fabricante.
     * Lista equipos de una marca ignorando mayúsculas/minúsculas.
     */
    List<Device> findByManufacturerIgnoreCase(String manufacturer);

    /*
     * REGLA DE NEGOCIO: Gestión de Mantenimiento / Dashboard.
     * Cuenta cuántos dispositivos tiene un usuario según su estado.
     * Ruta: Device -> Room -> Home -> User.
     */
    long countByRoom_Home_User_IdUserAndStatus(Long idUser, Integer status);

    /*
     * REGLA DE NEGOCIO: Buscador amigable en el Dashboard.
     * Permite al usuario buscar por el nombre asignado al dispositivo.
     */
    List<Device> findByNameContainingIgnoreCaseAndRoom_Home_IdHome(String name, Long idHome);
}
