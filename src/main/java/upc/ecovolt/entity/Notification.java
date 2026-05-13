package upc.ecovolt.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notifications")
public class Notification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_notification")
    private Long id;

    @Column(name = "notification_type", nullable = false, length = 50)
    private String notificationType; // Ej: "ALERT"

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "message", nullable = false, length = 255)
    private String message;

    @Column(name = "delivery_channel", nullable = false, length = 30)
    private String deliveryChannel; // Ej: "Push", "Email", "SMS", "Web"

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    @Column(name = "read_at")
    private LocalDateTime readAt;

    @Column(name = "notification_status", nullable = false, length = 30)
    private String notificationStatus; // Ej: "Read", "Unread"

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_alert")
    private Alert alert;
}