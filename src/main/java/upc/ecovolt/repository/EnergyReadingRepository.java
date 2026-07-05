package upc.ecovolt.repository;

import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import upc.ecovolt.entity.EnergyReading;
import upc.ecovolt.mapping.dto.ReporteCasaDTO;
import upc.ecovolt.mapping.dto.ReporteCuartoDTO;
import upc.ecovolt.mapping.dto.ReporteDispositivoDTO;

public interface EnergyReadingRepository extends JpaRepository<EnergyReading, Long> {

    @Query("SELECT new upc.ecovolt.mapping.dto.ReporteCasaDTO(r.home.alias, SUM(er.wattage)) " +
            "FROM EnergyReading er " +
            "JOIN er.device d " +
            "JOIN d.room r " +
            "WHERE er.device.room.home.user.idUser = ?1 " +
            "GROUP BY r.home.alias")
    List<ReporteCasaDTO> reporteConsumoPorCasa(Long idUser);

    @Query("SELECT new upc.ecovolt.mapping.dto.ReporteCuartoDTO(r.name, SUM(er.wattage)) " +
            "FROM EnergyReading er " +
            "JOIN er.device d " +
            "JOIN d.room r " +
            "WHERE r.home.idHome = ?1 " +
            "GROUP BY r.name")
    List<ReporteCuartoDTO> reporteConsumoPorCuarto(Long idHome);

    @Query("SELECT new upc.ecovolt.mapping.dto.ReporteDispositivoDTO(d.name, SUM(er.wattage)) " +
            "FROM EnergyReading er " +
            "JOIN er.device d " +
            "WHERE d.room.idRoom = ?1 " +
            "GROUP BY d.name")
    List<ReporteDispositivoDTO> reporteConsumoPorDispositivo(Long idRoom);

    /*
     * REGLA DE NEGOCIO: Cálculo de Consumo Acumulado por Dispositivo.
     * Retorna Optional para manejar casos donde no hay lecturas en el rango.
     */
    @Query("select sum(er.wattage) from EnergyReading er where er.device.idDevice = ?1 and er.readingAt between ?2 and ?3 and er.status = 1")
    Optional<BigDecimal> sumWattageByDeviceAndPeriod(Long idDevice, LocalDateTime start, LocalDateTime end);

    /*
     * REGLA DE NEGOCIO: Análisis de Consumo por Propiedad (Dashboard).
     * Navega: Reading -> Device -> Room -> Home.
     */
    @Query("select sum(er.wattage) from EnergyReading er where er.device.room.home.idHome = ?1 and er.readingAt between ?2 and ?3 and er.status = 1")
    Optional<BigDecimal> sumTotalConsumptionByHome(Long idHome, LocalDateTime start, LocalDateTime end);

    /*
     * REGLA DE NEGOCIO: Inteligencia por Categoría (Gráfico de torta/pie).
     * Filtra por descripción de categoría (Ej: 'Iluminación', 'Línea Blanca').
     */
    @Query("select sum(er.wattage) from EnergyReading er " +
            "where er.device.room.home.idHome = ?1 " +
            "and er.device.category.description = ?2 " +
            "and er.readingAt between ?3 and ?4 " +
            "and er.status = 1")
    Optional<BigDecimal> sumConsumptionByCategory(Long idHome, String categoryDescription, LocalDateTime start, LocalDateTime end);

    /*
     * REGLA DE NEGOCIO: Telemetría en Tiempo Real.
     * Uso de Pageable para que el Frontend pida solo las últimas N lecturas (ej: últimas 20).
     */
    List<EnergyReading> findByDevice_IdDeviceOrderByReadingAtDesc(Long idDevice, Pageable pageable);

    /*
     * REGLA DE NEGOCIO: Detección de Fugas / Consumo Fantasma.
     * Busca lecturas superiores a un umbral de ruido.
     */
    @Query("select er from EnergyReading er where er.device.idDevice = ?1 and er.wattage > ?2 and er.status = 1")
    List<EnergyReading> findAbnormalConsumption(Long idDevice, BigDecimal threshold);

    /*
     * REGLA DE NEGOCIO: Monitoreo de Calidad Eléctrica.
     * Promedio de voltaje para detectar fluctuaciones.
     */
    @Query("select avg(er.voltage) from EnergyReading er where er.device.idDevice = ?1")
    Optional<Double> getAverageVoltageByDevice(Long idDevice);
}