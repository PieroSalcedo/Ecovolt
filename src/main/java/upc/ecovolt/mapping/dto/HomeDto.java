package upc.ecovolt.mapping.dto;

import lombok.Data;
import java.math.BigDecimal;

public class HomeDto {
    @Data
    public static class Request {
        private String address;
        private String city;         // Agregado
        private String alias;        // Agregado (Importante para el error)
        private BigDecimal energyTariff; // Agregado
        private Integer squareMeters;    // Agregado
        private Integer idPropertyType;  // ID del catálogo
        private Long idUser;
    }

    @Data
    public static class Response {
        private Long idHome;
        private String address;
        private String city;         // Agregado
        private String alias;        // Agregado (Soluciona el error)
        private String ownerName;
    }
}