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
    /* REGLA DE NEGOCIO: Identificación visual en gráficas */
    private String deviceName;

    /* REGLA DE NEGOCIO: Time-series data.
     * Indica el momento exacto del consumo para las gráficas de línea.
     */
    private LocalDateTime createdAt;
}