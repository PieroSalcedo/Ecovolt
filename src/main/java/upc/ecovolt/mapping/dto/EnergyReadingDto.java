package upc.ecovolt.mapping.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class EnergyReadingDto {
    @Data
    public static class Request {
        private BigDecimal wattage;
        private BigDecimal voltage;
        private Long idDevice;
    }

    @Data
    public static class Response {
        private Long idReading;
        private BigDecimal wattage;
        private BigDecimal voltage;
        private LocalDateTime readingAt;
        private Integer status;
        // AGREGA ESTA LÍNEA:
        private Long idDevice;
    }
}