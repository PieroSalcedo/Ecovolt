package upc.ecovolt.mapping.dto;

import lombok.Data;
import java.math.BigDecimal;

public class EnergyGoalDto {
    @Data
    public static class Request {
        private BigDecimal monthlyLimitKwh;
        private Integer alertThresholdPercentage;
        private Long homeId;
    }

    @Data
    public static class Response {
        private Long idGoal;
        private BigDecimal monthlyLimitKwh;
        private Integer alertThresholdPercentage;
        private Long homeId;
        private String homeAlias;
        private String homeAddress;
        private Integer status;
    }
}
