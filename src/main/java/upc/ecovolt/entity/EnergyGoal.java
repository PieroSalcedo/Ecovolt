package upc.ecovolt.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "energy_goal")
public class EnergyGoal extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_goal")
    private Integer idGoal;

    /*
     * REGLA DE NEGOCIO: Presupuesto Energético Mensual.
     * Es el límite máximo en kWh que el usuario se impone para un periodo.
     * El sistema comparará la suma de 'energy_readings' contra este valor.
     */
    @Column(name = "monthly_limit_kwh", precision = 10, scale = 2)
    private BigDecimal monthlyLimitKwh;

    /*
     * REGLA DE NEGOCIO: Sistema de Alerta Temprana.
     * Define el umbral de aviso (Ej: 80%).
     * Cuando el consumo real llega al 80% del límite, el backend dispara una notificación.
     */
    @Column(name = "alert_threshold_percentage")
    private Integer alertThresholdPercentage = 80;

    /*
     * REGLA DE NEGOCIO: Ámbito de Aplicación.
     * Una meta de ahorro se aplica a una propiedad completa (Home).
     * Usamos Lazy Loading y JsonIgnoreProperties siguiendo el estándar del profesor.
     */
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_home", nullable = false)
    private Home home;

}
