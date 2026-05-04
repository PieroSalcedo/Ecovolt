package upc.ecovolt.entity;

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

    @Column(name = "address", nullable = false, length = 200)
    private String address;

    @Column(name = "city", nullable = false, length = 50)
    private String city;

    @Column(name = "alias", nullable = false, length = 50) // Ej: "Casa de Playa" o "Dpto San Isidro"
    private String alias;

    @Column(name = "energy_tariff", nullable = false, precision = 10, scale = 4)
    private BigDecimal energyTariff; // Costo por kWh (usamos 4 decimales para mayor precisión)

    @Column(name = "square_meters", precision = 10, scale = 2)
    private BigDecimal squareMeters;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", nullable = false)
    private User user;
}
