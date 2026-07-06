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
        private Integer squareMeters;
        private Integer idPropertyType;
        private Long idUser;
    }

    @Data
    public static class Response {
        private Long idHome;
        private String address;
        private String city;
        private String alias;
        private BigDecimal energyTariff;
        private Integer squareMeters;

        // ESTOS 3 CAMPOS SON LOS QUE HACÍAN FALLAR EL REBUILD:
        private Integer idPropertyType;   // Para que el lapicito cargue el combo
        private String propertyTypeName;  // Para mostrar 'Casa' o 'Depa'
        private String ownerName;         // Para identificar al dueño
    }
}