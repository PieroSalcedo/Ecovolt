package upc.ecovolt.mapping.dto.automationdto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class AutomationResponseDto {

    private Long id;
    private String automationName;
    private String automationType;
    private String action;
    private LocalTime startTime;
    private LocalTime endTime;
    private String weekDays;
    private String automationStatus;
    private Long userId;
    private Long deviceId;
    private Integer status;
}