package upc.ecovolt.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "automations")
public class Automation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_automation")
    private Long id;

    @Column(name = "automation_name", nullable = false, length = 100)
    private String automationName;

    @Column(name = "automation_type", nullable = false, length = 50)
    private String automationType; // Ej: "Schedule", "Energy Saving", "Security"

    @Column(name = "action", nullable = false, length = 50)
    private String action; // Ej: "TURN_ON", "TURN_OFF", "ACTIVATE"

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Column(name = "week_days", length = 100)
    private String weekDays; // Ej: "Monday,Tuesday,Friday"

    @Column(name = "automation_status", nullable = false, length = 30)
    private String automationStatus; // Ej: "Active", "Paused"

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_device", nullable = false)
    private Device device;
}