package upc.ecovolt.mapping.dto.notificationdto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class NotificationResponseDto {

    private Long id;
    private String notificationType;
    private String title;
    private String message;
    private String deliveryChannel;
    private LocalDateTime sentAt;
    private LocalDateTime readAt;
    private String notificationStatus;
    private Long userId;
    private Long alertId;
    private Integer status;
}