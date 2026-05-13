package upc.ecovolt.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "energy_reports")
public class EnergyReport extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_report")
    private Long id;

    @Column(name = "report_type", nullable = false, length = 50)
    private String reportType; // Ej: "Daily", "Weekly", "Monthly"

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "report_format", nullable = false, length = 20)
    private String reportFormat; // Ej: "PDF", "Excel"

    @Column(name = "file_path", length = 255)
    private String filePath;

    @Column(name = "generated_at")
    private LocalDateTime generatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_home", nullable = false)
    private Home home;
}