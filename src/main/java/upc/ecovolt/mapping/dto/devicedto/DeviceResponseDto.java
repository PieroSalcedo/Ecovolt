package upc.ecovolt.mapping.dto.devicedto;

import lombok.Data;

@Data
public class DeviceResponseDto {
    private Long id;
    private String serialNumber;
    private String name;
    private String category;
    private String manufacturer;
    private String firmwareVersion;
    private Long roomId;
    private Integer status;
}