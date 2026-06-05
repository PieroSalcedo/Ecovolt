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
        private Long idPlan;
        private String name;
        private String description;
        private BigDecimal price;
        private Integer durationDays;
        private Integer status;
    }
}
