package upc.ecovolt.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "energy_readings")
public class EnergyReading extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reading")
    private Long id;

    @Column(name = "wattage", nullable = false, precision = 10, scale = 2)
    private BigDecimal wattage; // Consumo real en Watts

    @Column(name = "voltage", nullable = false, precision = 10, scale = 2)
    private BigDecimal voltage; // Tensión eléctrica (Ej: 220.5V)

    @Column(name = "amperage", nullable = false, precision = 10, scale = 4)
    private BigDecimal amperage; // Corriente (Ej: 1.2504 A)

    @Column(name = "power_factor", nullable = false, precision = 4, scale = 3)
    private BigDecimal powerFactor; // Eficiencia (valor entre 0 y 1)

    @Column(name = "frequency", nullable = false, precision = 5, scale = 2)
    private BigDecimal frequency; // Frecuencia de red (En Perú: 60Hz)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_device", nullable = false)
    private Device device;
}
