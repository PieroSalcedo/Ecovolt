package upc.ecovolt.mapping.dto;

import lombok.Data;
import java.math.BigDecimal;

public class EnergyGoalDto {
    @Data
    public static class Request {
        private BigDecimal targetValue; // Mapea a monthlyLimitKwh
        private Integer alertThresholdPercentage;
        private Long idHome;
        private Long idRoom;
        private Long idDevice;
    }

    @Data
    public static class Response {
        private Integer idGoal;
        private BigDecimal targetValue;
        private Integer alertThresholdPercentage;
        private String targetName; // Alias de casa, nombre de cuarto o nombre de equipo
        private String type;       // "CASA", "CUARTO" o "DISPOSITIVO"
        private Integer status;
    }
}