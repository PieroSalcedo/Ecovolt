package upc.ecovolt.mapping.dto.energyreadingdto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class EnergyReadingRequestDto {

    /*
     * REGLA DE NEGOCIO: Variable de consumo activo.
     * Es el dato principal para el cálculo del recibo de luz.
     */
    @NotNull(message = "El wattage es obligatorio")
    @PositiveOrZero(message = "El consumo no puede ser negativo")
    private BigDecimal wattage;

    /* REGLA DE NEGOCIO: Monitoreo de calidad de red (Estándar 220V en Perú) */
    @NotNull(message = "El voltaje es obligatorio")
    @Positive(message = "El voltaje debe ser mayor a cero")
    private BigDecimal voltage;

    @NotNull @Positive
    private BigDecimal amperage;

    /* REGLA DE NEGOCIO: Eficiencia energética.
     * Debe estar entre 0.0 y 1.0 (Power Factor)
     */
    @NotNull
    @DecimalMin("0.0") @DecimalMax("1.0")
    private BigDecimal powerFactor;

    @NotNull @Positive
    private BigDecimal frequency;

    @NotNull(message = "El ID del dispositivo es obligatorio")
    private Long deviceId;
}