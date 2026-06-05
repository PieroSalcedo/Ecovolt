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
@Table(name = "room")
public class Room extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_room")
    private Long idRoom;

    /*
     * REGLA DE NEGOCIO: Etiqueta descriptiva.
     * Permite al usuario identificar la habitación en la interfaz (Ej: "Habitación de invitados").
     */
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    /*
     * REGLA DE NEGOCIO: Análisis de distribución vertical.
     * Útil para identificar si los pisos superiores consumen más energía
     * (por ejemplo, por mayor uso de aire acondicionado debido al calor del techo).
     */
    @Column(name = "floor_number")
    private Integer floorNumber;

    /*
     * REGLA DE NEGOCIO: Cálculo de densidad energética.
     * Factor clave para determinar si una habitación está "sub-optimizada"
     * calculando el ratio de Watts por metro cuadrado.
     */
    @Column(name = "area_sqm", precision = 10, scale = 2)
    private BigDecimal areaSqm;

    /*
     * REGLA DE NEGOCIO: Tipificación estandarizada (Estilo Profesor).
     * Relación con DataCatalogo para clasificar como "Cocina", "Sala", "Dormitorio", "Baño".
     * Es vital para reportes comparativos: "¿Las cocinas consumen más que los dormitorios?"
     */
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_room_type")
    private DataCatalog roomType;

    /*
     * REGLA DE NEGOCIO: Integridad Jerárquica.
     * Una habitación siempre debe pertenecer a una propiedad (Home).
     */
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_home", nullable = false)
    private Home home;

    /*
     * Opcional: El campo 'orientation' lo podemos dejar como String
     * o pasarlo a catálogo si el negocio requiere filtrar estrictamente
     * por "Norte", "Sur", "Este", "Oeste".
     */
    @Column(name = "orientation", length = 20)
    private String orientation;
}