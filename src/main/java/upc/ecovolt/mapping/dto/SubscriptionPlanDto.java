package upc.ecovolt.mapping.dto;

import lombok.Data;
import java.math.BigDecimal;

public class SubscriptionPlanDto {
    @Data
    public static class Request {
        private String name;
        private BigDecimal monthlyPrice;
        private Integer deviceLimit;
        private Integer supportLevelId;
        private String billingCycle;
    }

    @Data
    public static class Response {
        private Long idPlan;
        private String name;
        private BigDecimal monthlyPrice;
        private Integer deviceLimit;
        private Integer supportLevelId;
        private String supportLevelDescription;
        private String billingCycle;
        private Integer status;
    }
}
