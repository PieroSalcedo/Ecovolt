package upc.ecovolt.mapping.dto;

import lombok.Data;

public class UserDto {
    @Data
    public static class Request {
        private String login;
        private String email;
        private String password;
        private String firstName;
        private String lastName;
        private String phoneNumber;
    }

    @Data
    public static class Response {
        private Long idUser;
        private String login;
        private String email;
        private String firstName;
        private String lastName;
        private String phoneNumber;
        private String fullName;
        private Integer status;
    }
}
