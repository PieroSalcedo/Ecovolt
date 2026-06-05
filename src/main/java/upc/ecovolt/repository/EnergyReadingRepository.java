package upc.ecovolt.repository;

import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import upc.ecovolt.entity.EnergyReading;

@Repository
public interface EnergyReadingRepository extends JpaRepository<EnergyReading, Long> {

    /*
     * REGLA DE NEGOCIO: Cálculo de Consumo Acumulado.
     * Suma el wattage total de un dispositivo en un rango de fechas.
     * Es la base para calcular cuánto debe pagar el usuario.
     */
    @Query("select sum(er.wattage) from EnergyReading er where er.device.idDevice = ?1 and er.readingAt between ?2 and ?3")
    BigDecimal sumWattageByDeviceAndPeriod(Long idDevice, LocalDateTime start, LocalDateTime end);

    /*
     * REGLA DE NEGOCIO: Monitoreo de Calidad Eléctrica.
     * Obtiene el voltaje promedio de un dispositivo.
     * Ayuda a identificar problemas en la red eléctrica del hogar.
     */
    @Query("select avg(er.voltage) from EnergyReading er where er.device.idDevice = ?1")
    Double getAverageVoltageByDevice(Long idDevice);

    /*
     * REGLA DE NEGOCIO: Análisis de Consumo por Propiedad.
     * Suma todo el consumo de una casa completa navegando: Reading -> Device -> Room -> Home.
     * Es lo que se muestra en el gráfico principal del Dashboard.
     */
    @Query("select sum(er.wattage) from EnergyReading er where er.device.room.home.idHome = ?1 and er.readingAt between ?2 and ?3")
    BigDecimal sumTotalConsumptionByHome(Long idHome, LocalDateTime start, LocalDateTime end);

    /*
     * REGLA DE NEGOCIO: Telemetría en Tiempo Real.
     * Obtiene las últimas lecturas registradas de un dispositivo.
     */
    @Query("select er from EnergyReading er where er.device.idDevice = ?1 order by er.readingAt desc")
    List<EnergyReading> findLatestReadingsByDevice(Long idDevice);

    /*
     * REGLA DE NEGOCIO: Detección de Fugas / Consumo Fantasma.
     * Busca lecturas de wattage superiores a cero en horarios donde no debería haber consumo.
     */
    @Query("select er from EnergyReading er where er.device.idDevice = ?1 and er.wattage > ?2 and er.status = 1")
    List<EnergyReading> findAbnormalConsumption(Long idDevice, BigDecimal threshold);

    /*
     * REGLA DE NEGOCIO: Inteligencia por Categoría (DataCatalogo).
     * Suma el consumo de todos los dispositivos de una categoría (Ej: 'Iluminación') en un hogar.
     */
    @Query("select sum(er.wattage) from EnergyReading er " +
            "where er.device.room.home.idHome = ?1 " +
            "and er.device.category.description = ?2 " +
            "and er.readingAt between ?3 and ?4")
    BigDecimal sumConsumptionByCategory(Long idHome, String categoryDescription, LocalDateTime start, LocalDateTime end);
}