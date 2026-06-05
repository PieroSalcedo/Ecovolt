package upc.ecovolt.mapping.dto;

import lombok.Data;

public class DataCatalogDto {
    @Data
    public static class Request {
        private String value;     // Ej: "Refrigeradora"
        private Long idCatalog;   // ID del catálogo padre
    }

    @Data
    public static class Response {
        private Long idDataCatalog;
        private String value;
        private Long idCatalog;
        private String catalogName; // Nombre del catálogo padre para mostrar en tablas
        private Integer status;
    }
}
