package upc.ecovolt.mapping.dto;

import lombok.Data;
import java.math.BigDecimal;

public class SubscriptionPlanDto {
    @Data
    public static class Request {
        private String name;
        private String description;
        private BigDecimal price;
        private Integer durationDays;
    }

    @Data
    public static class Response {
        private Integer idPlan;
        private String name;
        private String description;
        private BigDecimal monthlyPrice;
        private Integer deviceLimit;
        private String billingCycle;
        private Integer status;
    }
}
