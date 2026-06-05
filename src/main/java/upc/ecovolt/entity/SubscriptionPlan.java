package upc.ecovolt.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "subscription_plans")
public class SubscriptionPlan extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_plan")
    private Integer id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    /*
     * REGLA DE NEGOCIO: Monetización del SaaS.
     * Define el costo base para el cálculo de ingresos proyectados.
     */
    @Column(name = "monthly_price", precision = 10, scale = 2)
    private BigDecimal monthlyPrice;

    /*
     * REGLA DE NEGOCIO: Restricción de escalabilidad (Device Quota).
     * Es el "hard limit" que el sistema validará al momento de registrar un nuevo equipo IoT.
     */
    @Column(name = "device_limit", nullable = false)
    private Integer deviceLimit;

    /*
     * REGLA DE NEGOCIO: SLA (Service Level Agreement).
     * Relacionado a DataCatalogo para definir niveles (Soporte técnico, respuesta en horas, etc.).
     * Permite cambiar los beneficios del nivel sin alterar la tabla de planes.
     */
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "support_level_id")
    private DataCatalog supportLevel;

    /*
     * REGLA DE NEGOCIO: Modelo de recurrencia.
     * Define si el cobro es 'Mensual', 'Semestral' o 'Anual'.
     */
    @Column(name = "billing_cycle", length = 20)
    private String billingCycle;

}