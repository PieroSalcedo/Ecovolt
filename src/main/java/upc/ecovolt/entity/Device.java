package upc.ecovolt.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "device")
public class Device extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_device")
    private Long idDevice;

    /*
     * REGLA DE NEGOCIO: Identificador de Hardware Único (MAC Address / UUID).
     * Es la llave natural que permite vincular el dispositivo físico con la nube.
     * No se puede repetir en todo el ecosistema Ecovolt.
     */
    @Column(name = "serial_number", nullable = false, unique = true, length = 100)
    private String serialNumber;

    /*
     * REGLA DE NEGOCIO: Identificador amigable.
     * Nombre que el usuario asigna (Ej: "Refrigeradora LG") para identificarlo en el dashboard.
     */
    @Column(name = "name", length = 100)
    private String name;

    /*
     * REGLA DE NEGOCIO: Segmentación Energética (Estilo Profesor).
     * Uso de DataCatalogo para clasificar en: 'Iluminación', 'Climatización', 'Línea Blanca'.
     * Sirve para que el motor de IA de Ecovolt identifique qué categoría consume más.
     */
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_category")
    private DataCatalog category;

    /*
     * REGLA DE NEGOCIO: Trazabilidad de Fabricante.
     * Permite identificar si ciertas marcas presentan mayores fallos o consumo excesivo.
     */
    @Column(name = "manufacturer", length = 100)
    private String manufacturer;

    /*
     * REGLA DE NEGOCIO: Ciclo de vida de software del hardware.
     * Permite gestionar actualizaciones remotas (OTA) y asegurar compatibilidad de protocolos.
     */
    @Column(name = "firmware_version", length = 50)
    private String firmwareVersion;

    @Column(name = "on_off", nullable = false)
    private Boolean onOff = false;

    /*
     * REGLA DE NEGOCIO: Ubicación Contextual.
     * Un dispositivo siempre debe estar ubicado en una habitación para calcular
     * el consumo por áreas de la vivienda.
     */
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_room", nullable = false)
    private Room room;
}