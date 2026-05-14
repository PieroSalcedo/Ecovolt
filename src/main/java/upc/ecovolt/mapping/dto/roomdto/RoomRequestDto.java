package upc.ecovolt.mapping.dto.roomdto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter @Setter
public class RoomRequestDto {
    @NotBlank(message = "El nombre del ambiente es obligatorio")
    @Size(max = 100)
    private String name;

    /* REGLA DE NEGOCIO: Análisis de distribución por niveles */
    @NotNull(message = "El número de piso es obligatorio")
    private Integer floorNumber;

    private String orientation;

    /* REGLA DE NEGOCIO: Cálculo de densidad energética (W/m2) */
    @NotNull(message = "El área es obligatoria")
    @Positive(message = "El área debe ser mayor a 0")
    private BigDecimal areaSqm;

    /* Relación con el diccionario (DataCatalogo) */
    @NotNull(message = "El tipo de ambiente es obligatorio")
    private Integer roomTypeId;

    @NotNull(message = "El ID de la vivienda es obligatorio")
    private Long homeId;
}