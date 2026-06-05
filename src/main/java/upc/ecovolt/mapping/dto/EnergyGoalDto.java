package upc.ecovolt.mapping.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

public class EnergyGoalDto {
    @Data
    public static class Request {
        private BigDecimal targetValue; // El valor de la meta (ej. no pasar de 200 kWh)
        private LocalDate startDate;
        private LocalDate endDate;
        private Long idHome;           // ID de la casa a la que aplica la meta
    }

    @Data
    public static class Response {
        private Long idGoal;
        private BigDecimal targetValue;
        private LocalDate startDate;
        private LocalDate endDate;
        private Long idHome;
        private String homeAddress;    // Para identificar la casa fácilmente
        private Integer status;
    }
}
