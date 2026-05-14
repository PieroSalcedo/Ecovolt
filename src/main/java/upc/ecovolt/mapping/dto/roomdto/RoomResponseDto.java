package upc.ecovolt.mapping.dto.roomdto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter @Setter
public class RoomResponseDto {
    private Long id;
    private String name;
    private Integer floorNumber;
    private String orientation;
    private BigDecimal areaSqm;

    /* REGLA DE NEGOCIO: Mostrar el tipo de habitación (Cocina, Sala, etc.) */
    private String roomTypeName;

    private Long homeId;
    private Integer status;
}