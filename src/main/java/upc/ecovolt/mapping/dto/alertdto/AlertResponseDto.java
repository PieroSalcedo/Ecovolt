package upc.ecovolt.mapping.dto.alertdto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AlertResponseDto {

    private Long id;
    private String alertType;
    private String title;
    private String description;
    private String severityLevel;
    private LocalDateTime alertDate;
    private String alertStatus;
    private Long deviceId;
    private Integer status;
}