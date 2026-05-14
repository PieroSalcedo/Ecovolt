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
@Table(name = "homes")
public class Home extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_home")
    private Long id;

    /*
     * REGLA DE NEGOCIO: Ubicación física para geolocalización.
     * Permite en el futuro integrar servicios de clima (como OpenWeather)
     * para optimizar el uso de aire acondicionado/calefacción.
     */
    @Column(name = "address", columnDefinition = "TEXT")
    private String address;

    @Column(name = "city", length = 100)
    private String city;

    /*
     * REGLA DE NEGOCIO: Identificador amigable para el usuario.
     * Facilita la gestión multi-propiedad (Ej: "Oficina Principal", "Casa de Campo").
     */
    @Column(name = "alias", length = 50)
    private String alias;

    /*
     * REGLA DE NEGOCIO: Motor de conversión financiera.
     * Es el valor crítico para transformar los datos de telemetría (Watts)
     * en costo real monetario según la tarifa local del usuario.
     */
    @Column(name = "energy_tariff", precision = 10, scale = 4)
    private BigDecimal energyTariff;

    /*
     * REGLA DE NEGOCIO: Benchmark de Eficiencia.
     * Permite calcular el consumo por metro cuadrado (kWh/m2) para
     * comparar la eficiencia energética entre diferentes propiedades.
     */
    @Column(name = "square_meters")
    private Integer squareMeters;

    /*
     * REGLA DE NEGOCIO: Clasificación Estructural.
     * Uso de DataCatalogo para definir si es "Departamento", "Casa", "Local Comercial" o "Industria".
     * Esto permite al sistema aplicar algoritmos de ahorro específicos por tipo de inmueble.
     */
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipo_vivienda")
    private DataCatalogo propertyType;

    /*
     * REGLA DE NEGOCIO: Propiedad y Seguridad de Datos.
     * Garantiza que solo el dueño de la casa (o un Admin) pueda ver las lecturas.
     */
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", nullable = false)
    private User user;
}