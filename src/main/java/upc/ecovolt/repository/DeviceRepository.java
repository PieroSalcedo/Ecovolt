package upc.ecovolt.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import upc.ecovolt.entity.Device;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {

    /*
     * REGLA DE NEGOCIO: Identificación Única de Hardware.
     * Busca un dispositivo por su número de serie (MAC/UUID).
     */
    @Query("select d from Device d where d.serialNumber = ?1")
    Optional<Device> findBySerialNumber(String serialNumber);

    /*
     * REGLA DE NEGOCIO: Segmentación por Categoría (DataCatalogo).
     * Ejemplo: "Tráeme todos los dispositivos de 'Iluminación'".
     */
    @Query("select d from Device d where d.category.description = ?1 and d.status = 1")
    List<Device> findByCategoryName(String categoryDescription);

    /*
     * REGLA DE NEGOCIO: Inventario por Habitación.
     * Lista los equipos instalados en un ambiente específico.
     */
    @Query("select d from Device d where d.room.id = ?1 and d.status = 1")
    List<Device> findByRoomId(Long idRoom);

    /*
     * REGLA DE NEGOCIO: Inventario por Propiedad.
     * Lista todos los dispositivos de una casa (Device -> Room -> Home).
     */
    @Query("select d from Device d where d.room.home.id = ?1 and d.status = 1")
    List<Device> findByHomeId(Long idHome);

    /*
     * REGLA DE NEGOCIO: Análisis de Fabricante.
     * Lista equipos de una marca específica.
     */
    @Query("select d from Device d where d.manufacturer = ?1")
    List<Device> findByManufacturer(String manufacturer);

    /*
     * REGLA DE NEGOCIO: Gestión de Mantenimiento.
     * Cuenta cuántos dispositivos tiene un usuario en un estado específico.
     */
    @Query("select count(d) from Device d where d.room.home.user.id = ?1 and d.status = ?2")
    long countByUserIdAndStatus(Long idUser, Integer status);
}
