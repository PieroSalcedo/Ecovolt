package upc.ecovolt.mapping.dto;

import lombok.Data;

public class DeviceDto {
    @Data
    public static class Request {
        private String name;
        private String serialNumber;
        private String brand;
        private Long idRoom;
        private Integer idCategory; // <-- NUEVO: Para recibir la categoría del combo
    }

    @Data
    public static class Response {
        private Long idDevice;
        private String name;
        private String serialNumber;
        private String brand;
        private String roomName;
        private Long idRoom;
        private Integer idCategory;    // <-- NUEVO
        private String categoryName;  // <-- NUEVO: Para mostrar 'Línea Blanca', etc.
        private String firmwareVersion; // <-- NUEVO
        private Integer status;
    }
}