package upc.ecovolt.mapping.dto.devicedto;

import lombok.Data;

@Data
public class DeviceResponseDto {
    private Long id;
    private String serialNumber;
    private String name;

    /* Aquí el usuario verá "Climatización" o "Iluminación" */
    private String categoryName;

    private String manufacturer;
    private String firmwareVersion;
    private Long roomId;
    private Integer status;
}