package upc.ecovolt.mapping.dto.roomdto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter
public class RoomRequestDto {
    @NotBlank(message = "El nombre del ambiente es obligatorio")
    @Size(max = 50)
    private String name;

    @NotNull(message = "El número de piso es obligatorio")
    private Integer floorNumber;

    private String orientation;

    @Positive(message = "El área debe ser mayor a 0")
    private BigDecimal areaSqm;

    private String roomType;

    @NotNull(message = "El ID de la vivienda es obligatorio")
    private Long homeId;
}