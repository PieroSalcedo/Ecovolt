package upc.ecovolt.mapping.dto;

import lombok.Data;

public class OptionDto {
    @Data
    public static class Request {
        private String name;        // Ej: "GESTION_DISPOSITIVOS"
        private String description; // Ej: "Permite agregar y editar dispositivos"
        private String url;         // Ruta del frontend si aplica
    }

    @Data
    public static class Response {
        private Long idOption;
        private String name;
        private String description;
        private String url;
        private Integer status;
    }
}
