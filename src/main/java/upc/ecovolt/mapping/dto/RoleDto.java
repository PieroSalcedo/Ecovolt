package upc.ecovolt.mapping.dto;

import lombok.Data;

public class RoleDto {
    @Data
    public static class Request {
        private String name;
    }

    @Data
    public static class Response {
        private Long idRole;
        private String name;
        private Integer status;
    }
}
