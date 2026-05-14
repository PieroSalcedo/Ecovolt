package upc.ecovolt.mapping.dto.homedto;

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
    private Integer squareMeters;
    private Long userId;

    /* REGLA DE NEGOCIO: Mostrar el tipo de vivienda (Casa, Dpto, Local) */
    private String propertyTypeName;

    private Integer status;
}