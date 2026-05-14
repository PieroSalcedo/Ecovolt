package upc.ecovolt.mapping.dto.subscriptionplandto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class SubscriptionPlanRequestDto {

    @NotBlank(message = "El nombre del plan es obligatorio")
    @Size(max = 50)
    private String name;

    @NotNull(message = "El precio mensual es obligatorio")
    @PositiveOrZero(message = "El precio no puede ser negativo")
    private BigDecimal monthlyPrice;

    /* REGLA DE NEGOCIO: Límite de equipos para el modelo SaaS */
    @NotNull(message = "El límite de dispositivos es obligatorio")
    @Min(value = 1, message = "El límite debe ser al menos 1")
    private Integer deviceLimit;

    /*
     * Recibimos el ID del DataCatalogo (ej: 15)
     * que representa el nivel de soporte.
     */
    @NotNull(message = "El ID del nivel de soporte es obligatorio")
    private Integer supportLevelId;

    @NotBlank(message = "El ciclo de facturación es obligatorio")
    private String billingCycle;
}