package upc.ecovolt.mapping.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

public class EnergyAdvisorDto {

    @Data
    public static class Request {
        private Long idHome;
        private String period;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private String summary;
        private String riskLevel;
        private BigDecimal totalKwh;
        private BigDecimal estimatedCost;
        private BigDecimal monthlyGoalKwh;
        private BigDecimal goalProgressPercentage;
        private BigDecimal estimatedSaving;
        private Boolean aiGenerated;
        private List<Metric> metrics;
        private List<Recommendation> recommendations;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Metric {
        private String label;
        private String type;
        private BigDecimal value;
        private String unit;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Recommendation {
        private String title;
        private String description;
        private String priority;
        private String category;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GeminiPayload {
        private String summary;
        private String riskLevel;
        private BigDecimal estimatedSaving;
        private List<Recommendation> recommendations;
    }
}
