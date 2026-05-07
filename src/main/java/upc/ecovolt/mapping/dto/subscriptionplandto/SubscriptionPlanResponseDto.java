package upc.ecovolt.mapping.dto.subscriptionplandto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SubscriptionPlanResponseDto {

    //Lo que al usuario le va a llegar
    private Long id;
    private String name;
    private BigDecimal monthlyPrice;
    private Integer deviceLimit;
    private String supportLevel;
    private String billingCycle;
    private Integer status;
}
