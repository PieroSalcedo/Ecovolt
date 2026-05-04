package upc.ecovolt.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "devices")
public class Device extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_device")
    private Long id;

    @Column(name = "serial_number", nullable = false, unique = true, length = 100)
    private String serialNumber; // El "DNI" del hardware (MAC Address o UUID)

    @Column(name = "device_name", nullable = false, length = 100)
    private String name; // Ej: "Refrigeradora Principal"

    @Column(name = "category", nullable = false, length = 50)
    private String category; // Ej: "Climatización", "Iluminación", "Electrodomésticos"

    @Column(name = "manufacturer", length = 50)
    private String manufacturer; // Marca del equipo

    @Column(name = "firmware_version", length = 20)
    private String firmwareVersion;

    // RELACIÓN: Muchos dispositivos están en un mismo ambiente (Room)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_room", nullable = false)
    private Room room;
}
