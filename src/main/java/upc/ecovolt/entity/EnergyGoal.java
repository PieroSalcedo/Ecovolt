package upc.ecovolt.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity @Table(name = "energy_goal")
public class EnergyGoal extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_goal")
    private Integer idGoal;

    @Column(name = "monthly_limit_kwh", precision = 10, scale = 2)
    private BigDecimal monthlyLimitKwh;

    @Column(name = "alert_threshold_percentage")
    private Integer alertThresholdPercentage = 80;

    // NIVEL 1: VIVIENDA (Nullable)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_home", nullable = true)
    private Home home;

    // NIVEL 2: CUARTO (Nullable)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_room", nullable = true)
    private Room room;

    // NIVEL 3: DISPOSITIVO (Nullable)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_device", nullable = true)
    private Device device;
}