package upc.ecovolt.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "alerts")
public class Alert extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_alert")
    private Long id;

    @Column(name = "alert_type", nullable = false, length = 50)
    private String alertType; // Ej: "OVERCONSUMPTION", "VOLTAGE", "CONNECTION"

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "severity_level", nullable = false, length = 30)
    private String severityLevel; // Ej: "Low", "Medium", "High"

    @Column(name = "alert_date")
    private LocalDateTime alertDate;

    @Column(name = "alert_status", nullable = false, length = 30)
    private String alertStatus; // Ej: "Pending", "Resolved"

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_device", nullable = false)
    private Device device;
}
