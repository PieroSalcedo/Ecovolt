package upc.ecovolt.mapping.dto.devicedto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DeviceRequestDto {
    @NotBlank(message = "El número de serie es obligatorio")
    @Size(max = 100)
    private String serialNumber;

    @NotBlank(message = "El nombre del dispositivo es obligatorio")
    @Size(max = 100)
    private String name;

    @NotBlank(message = "La categoría es obligatoria")
    private String category; // Ej: "Climatización"

    private String manufacturer;

    private String firmwareVersion;

    @NotNull(message = "El ID del ambiente (Room) es obligatorio")
    private Long roomId;
}