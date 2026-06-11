package upc.ecovolt.mapping.dto;

import lombok.Data;

public class DeviceDto {
    @Data
    public static class Request {
        private String serialNumber;
        private String name;
        private String manufacturer;
        private String firmwareVersion;
        private Integer categoryId;
        private Long roomId;
    }

    @Data
    public static class Response {
        private Long idDevice;
        private String serialNumber;
        private String name;
        private String manufacturer;
        private String firmwareVersion;
        private Integer categoryId;
        private String categoryDescription;
        private Long roomId;
        private String roomName;
        private Integer status;
    }
}
