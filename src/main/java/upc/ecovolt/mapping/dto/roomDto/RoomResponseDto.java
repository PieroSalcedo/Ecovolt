package upc.ecovolt.mapping.dto.roomDto;

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
    private String roomType;
    private Long homeId; // Para saber a qué casa pertenece
    private Integer status;
}