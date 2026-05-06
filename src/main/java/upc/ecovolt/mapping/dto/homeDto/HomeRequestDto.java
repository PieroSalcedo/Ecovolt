package upc.ecovolt.mapping.dto.homeDto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter
public class HomeRequestDto {

    private String address;
    private String city;
    private String alias; // Ej: "Casa de Playa"
    private BigDecimal energyTariff;
    private BigDecimal squareMeters;
    private Long userId;
}