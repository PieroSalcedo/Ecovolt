package upc.ecovolt.mapping.dto.homedto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter @Setter
public class HomeRequestDto {

    @NotBlank(message = "La dirección es obligatoria")
    private String address;

    @NotBlank(message = "La ciudad es obligatoria")
    private String city;

    @NotBlank(message = "El alias es obligatorio (Ej: Casa, Oficina)")
    private String alias;

    /*
     * REGLA DE NEGOCIO: Base para el cálculo de costos.
     * Sin una tarifa válida, el sistema no puede convertir Watts a Soles/Dólares.
     */
    @NotNull(message = "La tarifa de energía es obligatoria")
    @DecimalMin(value = "0.0001", message = "La tarifa debe ser mayor a 0")
    private BigDecimal energyTariff;

    /* REGLA DE NEGOCIO: Benchmark de eficiencia (kWh/m2) */
    @NotNull(message = "El área en m2 es obligatoria")
    @Positive(message = "El área debe ser un número positivo")
    private Integer squareMeters;

    @NotNull(message = "El ID de usuario es obligatorio")
    private Long userId;

    /* Relación con DataCatalogo (Ej: 1=Casa, 2=Dpto) */
    @NotNull(message = "El tipo de propiedad es obligatorio")
    private Integer propertyTypeId;
}