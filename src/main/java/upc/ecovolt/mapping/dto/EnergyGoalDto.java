package upc.ecovolt.mapping.dto;

import lombok.Data;
import java.math.BigDecimal;

public class EnergyGoalDto {
    @Data
    public static class Request {
        private BigDecimal targetValue; // Mapea monthlyLimitKwh
        private Integer alertThresholdPercentage;
        private Long idHome;
    }

    @Data
    public static class Response {
        private Integer idGoal;
        private BigDecimal targetValue;
        private Integer alertThresholdPercentage;
        private Long idHome;
        private String homeAddress;
        private Integer status;
    }
}