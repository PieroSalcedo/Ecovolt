package upc.ecovolt.mapping.dto;

import lombok.Data;

public class HomeDto {
    @Data
    public static class Request {
        private String address;
        private Long idUser; // Solo el ID para vincular
    }

    @Data
    public static class Response {
        private Long idHome;
        private String address;
        private String ownerName; // Para mostrar quién es el dueño sin devolver todo el objeto User
    }
}
