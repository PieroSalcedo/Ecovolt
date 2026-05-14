package upc.ecovolt.mapping.dto.energygoaldto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class EnergyGoalRequestDto {

    /* REGLA DE NEGOCIO: El límite mensual de consumo en kWh */
    @NotNull(message = "El límite mensual es obligatorio")
    @Positive(message = "El límite debe ser mayor a 0")
    private BigDecimal monthlyLimitKwh;

    /* REGLA DE NEGOCIO: Umbral para disparar notificaciones (Ej: 80%) */
    @Min(1) @Max(100)
    private Integer alertThresholdPercentage;

    @NotNull(message = "El ID de la vivienda es obligatorio")
    private Long homeId;
}