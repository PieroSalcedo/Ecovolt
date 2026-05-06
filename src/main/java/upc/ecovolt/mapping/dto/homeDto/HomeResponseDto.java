package upc.ecovolt.mapping.dto.homeDto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter
public class HomeResponseDto {
    private Long id;
    private String address;
    private String city;
    private String alias;
    private BigDecimal energyTariff;
    private BigDecimal squareMeters;
    private Long userId; // Para que el front sepa a quién pertenece
    private Integer status;
}