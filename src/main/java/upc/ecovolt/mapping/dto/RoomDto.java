package upc.ecovolt.mapping.dto;

import lombok.Data;
import java.math.BigDecimal;

public class RoomDto {
    @Data
    public static class Request {
        private String name;
        private Integer floorNumber;
        private BigDecimal areaSqm;
        private String orientation;
        private Integer roomTypeId;
        private Long homeId;
    }

    @Data
    public static class Response {
        private Long idRoom;
        private String name;
        private Integer floorNumber;
        private BigDecimal areaSqm;
        private String orientation;
        private Integer roomTypeId;
        private String roomTypeDescription;
        private Long homeId;
        private String homeAddress;
        private Integer status;
    }
}
