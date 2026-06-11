package upc.ecovolt.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "data_catalog")
public class DataCatalog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_data_catalog")
    private Integer idDataCatalog;

    /*
     * REGLA DE NEGOCIO: Estandarización de Opciones.
     * Almacena el valor específico que el usuario seleccionará.
     * Ejemplos: 'Cocina', 'Iluminación', 'Soporte 24/7', 'Activo'.
     * Garantiza que no existan errores tipográficos en los reportes de consumo.
     */
    @Column(name = "description", nullable = false, length = 200)
    private String description;

    /*
     * REGLA DE NEGOCIO: Clasificación Jerárquica.
     * Vincula este valor con su categoría maestra (Catalogo).
     * El uso de FetchType.LAZY y @JsonIgnoreProperties es el estándar
     * solicitado por el profesor para optimizar el rendimiento de la aplicación.
     */
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_catalog", nullable = false)
    private Catalog catalog;

}
