package upc.ecovolt.mapping.dto;

import lombok.Data;

public class UserDto {
    @Data
    public static class Request {
        private String name;
        private String email;
        private String password;
    }

    @Data
    public static class Response {
        private Long idUser;
        private String name;
        private String email;
        private Integer status;
    }
}
