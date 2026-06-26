package upc.ecovolt.mapping.dto;

import lombok.Data;
import java.math.BigDecimal;

public class SubscriptionPlanDto {
    @Data
    public static class Request {
        private String name;
        private String description;
        private BigDecimal price;
        private Integer durationDays; // Duración del plan
    }

    @Data
    public static class Response {
        private Integer idPlan;
        private String name;
        private String description;
        private BigDecimal monthlyPrice; // Ojo aquí: monthlyPrice
        private Integer deviceLimit;    // Ojo aquí: deviceLimit
        private String billingCycle;    // Ojo aquí: billingCycle
        private Integer status;
    }
}
