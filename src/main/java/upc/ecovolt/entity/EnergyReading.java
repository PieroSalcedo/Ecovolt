package upc.ecovolt.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "energy_reading")
public class EnergyReading {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reading")
    private Long idReading;

    /*
     * REGLA DE NEGOCIO: Variable Crítica de Facturación.
     * Es la potencia activa consumida. Este valor es el que se suma
     * y se multiplica por la 'energy_tariff' de la casa para generar el recibo.
     */
    @Column(name = "wattage", nullable = false, precision = 12, scale = 4)
    private BigDecimal wattage;

    /*
     * REGLA DE NEGOCIO: Calidad del Servicio Eléctrico.
     * Permite detectar fluctuaciones de tensión que podrían dañar equipos.
     * En Perú, el estándar es 220V.
     */
    @Column(name = "voltage", precision = 10, scale = 2)
    private BigDecimal voltage;

    /*
     * REGLA DE NEGOCIO: Auditoría Ligera (Time-Series).
     * En lugar de heredar de BaseEntity, usamos solo la fecha de registro.
     * Es la "línea de tiempo" para las gráficas del Dashboard.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "reading_at", updatable = false)
    private LocalDateTime readingAt;

    /*
     * REGLA DE NEGOCIO: Estado de Integridad.
     * Permite anular lecturas que se consideren "ruido" o errores de sensor
     * sin eliminarlas físicamente de la base de datos.
     */
    @Column(name = "status")
    private Integer status = 1;

    /*
     * REGLA DE NEGOCIO: Trazabilidad de Origen.
     * Vincula la lectura con el hardware específico que la generó.
     */
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_device", nullable = false)
    private Device device;

    @PrePersist
    protected void onCreate() {
        this.readingAt = LocalDateTime.now();
    }
}