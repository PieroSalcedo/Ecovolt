package upc.ecovolt.mapping.dto;

import lombok.Data;

public class RoleDto {
    @Data
    public static class Request {
        private String name;        // Ej: "ADMIN"
        private String description; // Ej: "Administrador del sistema"
    }

    @Data
    public static class Response {
        private Long idRole;
        private String name;
        private String description;
        private Integer status;
    }
}
