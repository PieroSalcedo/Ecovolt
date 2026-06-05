package upc.ecovolt.mapping.dto;

import lombok.Data;

public class RoomDto {
    @Data
    public static class Request {
        private String name;
        private Long idHome; // Para saber a qué casa pertenece
    }

    @Data
    public static class Response {
        private Long idRoom;
        private String name;
        private Long idHome;
        private String homeAddress; // Agregado para saber el nombre/dirección de la casa
        private Integer status;
    }
}
