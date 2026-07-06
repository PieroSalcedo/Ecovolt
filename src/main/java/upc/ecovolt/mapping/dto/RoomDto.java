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
        private Integer idRoomType; // FK para el combo
        private Long idHome;        // FK para la casa
    }

    @Data
    public static class Response {
        private Long idRoom;
        private String name;
        private Integer floorNumber;
        private BigDecimal areaSqm;
        private String orientation;
        private Long idHome;
        private String homeAddress;
        private Integer idRoomType;
        private String roomTypeName; // Para mostrar "Sala", "Cocina" en la tabla
        private Integer status;
    }
}
