package upc.ecovolt.mapping.dto;

import lombok.Data;

public class OptionDto {
    @Data
    public static class Request {
        private String name;
        private String route;
        private Integer type;
    }

    @Data
    public static class Response {
        private Integer idOption;
        private String name;
        private String route;
        private Integer type;
        private Integer status;
    }
}
