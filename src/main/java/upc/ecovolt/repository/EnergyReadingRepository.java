package upc.ecovolt.repository;

import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import upc.ecovolt.entity.EnergyReading;

public interface EnergyReadingRepository extends JpaRepository<EnergyReading, Long> {

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

    /*
     * REGLA DE INNOVACIÃ“N: Ranking de consumo por dispositivo.
     * Alimenta el Smart Advisor para detectar quÃ© equipo impacta mÃ¡s el recibo.
     */
    @Query("select er.device.name, sum(er.wattage) from EnergyReading er " +
            "where er.device.room.home.idHome = ?1 " +
            "and er.readingAt between ?2 and ?3 " +
            "and er.status = 1 " +
            "group by er.device.name " +
            "order by sum(er.wattage) desc")
    List<Object[]> findTopDeviceConsumptionByHome(Long idHome, LocalDateTime start, LocalDateTime end, Pageable pageable);

    /*
     * REGLA DE INNOVACIÃ“N: Ranking de consumo por ambiente.
     * Permite que la IA recomiende acciones por zona de la vivienda.
     */
    @Query("select er.device.room.name, sum(er.wattage) from EnergyReading er " +
            "where er.device.room.home.idHome = ?1 " +
            "and er.readingAt between ?2 and ?3 " +
            "and er.status = 1 " +
            "group by er.device.room.name " +
            "order by sum(er.wattage) desc")
    List<Object[]> findTopRoomConsumptionByHome(Long idHome, LocalDateTime start, LocalDateTime end, Pageable pageable);
}
