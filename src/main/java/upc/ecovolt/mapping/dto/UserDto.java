package upc.ecovolt.mapping.dto;

import lombok.Data;

public class UserDto {
    @Data
    public static class Request {
        private String firstName;
        private String lastName;
        private String email;
        private String login;
        private String password;
        private Integer idPlan;
    }

    @Data
    public static class Response {
        private Long idUser;
        private String login;
        private String firstName;
        private String lastName;
        private String email;
        private Integer status;
    }
}