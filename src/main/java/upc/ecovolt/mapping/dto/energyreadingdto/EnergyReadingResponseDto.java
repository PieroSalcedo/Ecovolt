package upc.ecovolt.mapping.dto.energyreadingdto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class EnergyReadingResponseDto {
    private Long id;
    private BigDecimal wattage;
    private BigDecimal voltage;
    private BigDecimal amperage;
    private BigDecimal powerFactor;
    private BigDecimal frequency;
    private Long deviceId;
    private LocalDateTime createdAt; // Actúa como el Timestamp de la lectura
}