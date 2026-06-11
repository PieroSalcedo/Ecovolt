package upc.ecovolt.mapping.dto;

import lombok.Data;
import java.math.BigDecimal;

public class HomeDto {
    @Data
    public static class Request {
        private String address;
        private String city;
        private String alias;
        private BigDecimal energyTariff;
        private BigDecimal squareMeters;
        private Integer propertyTypeId;
        private Long userId;
    }

    @Data
    public static class Response {
        private Long idHome;
        private String address;
        private String city;
        private String alias;
        private BigDecimal energyTariff;
        private Integer squareMeters;
        private Integer propertyTypeId;
        private String propertyTypeDescription;
        private Long userId;
        private String ownerName;
        private Integer status;
    }
}
