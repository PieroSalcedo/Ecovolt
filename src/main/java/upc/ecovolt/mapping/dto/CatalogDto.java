package upc.ecovolt.mapping.dto;

import lombok.Data;

public class CatalogDto {
    @Data
    public static class Request {
        private String code; // Ej: "DEV_TYPE"
        private String name; // Ej: "Tipo de Dispositivo"
    }

    @Data
    public static class Response {
        private Long idCatalog;
        private String code;
        private String name;
        private Integer status;
    }
}
