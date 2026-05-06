package upc.ecovolt.mapping.dto.subscriptionplandto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SubscriptionPlanRequestDto {

    private String name;
    private BigDecimal monthlyPrice;
    private Integer deviceLimit;
    private String supportLevel;
    private String billingCycle;
}
