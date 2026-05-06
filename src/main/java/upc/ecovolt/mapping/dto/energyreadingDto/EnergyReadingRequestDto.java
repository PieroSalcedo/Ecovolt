package upc.ecovolt.mapping.dto.energyreadingDto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class EnergyReadingRequestDto {
    @NotNull @PositiveOrZero
    private BigDecimal wattage;

    @NotNull @Positive
    private BigDecimal voltage;

    @NotNull @Positive
    private BigDecimal amperage;

    @NotNull @DecimalMin("0.0") @DecimalMax("1.0")
    private BigDecimal powerFactor;

    @NotNull @Positive
    private BigDecimal frequency;

    @NotNull(message = "El ID del dispositivo es obligatorio")
    private Long deviceId; // Trazabilidad con el sensor
}