package upc.ecovolt.mapping.dto;

import lombok.Data;

public class DeviceDto {
    @Data
    public static class Request {
        private String name;
        private String brand;
        private Long idRoom;
    }

    @Data
    public static class Response {
        private Long idDevice;
        private String name;
        private String brand;
        private String roomName;
        private Integer status;
    }
}
