package upc.ecovolt.mapping.dto.energygoaldto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class EnergyGoalResponseDto {
    private Integer id;
    private BigDecimal monthlyLimitKwh;
    private Integer alertThresholdPercentage;
    private Long homeId;
    private String homeAlias; // Para que el usuario sepa de qué casa es la meta
    private Integer status;
}