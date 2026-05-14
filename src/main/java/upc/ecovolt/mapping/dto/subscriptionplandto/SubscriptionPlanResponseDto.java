package upc.ecovolt.mapping.dto.subscriptionplandto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class SubscriptionPlanResponseDto {
    private Integer id;
    private String name;
    private BigDecimal monthlyPrice;
    private Integer deviceLimit;

    /* Aquí el usuario verá el texto (ej: "Premium", "Básico") */
    private String supportLevel;

    private String billingCycle;
    private Integer status;
}