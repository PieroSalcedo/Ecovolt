package upc.ecovolt.mapping.dto.energyreportdto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class EnergyResponseDto {

    private Long id;
    private String reportType;
    private LocalDate startDate;
    private LocalDate endDate;
    private String reportFormat;
    private String filePath;
    private LocalDateTime generatedAt;
    private Long userId;
    private Long homeId;
    private Integer status;
}