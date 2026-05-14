package upc.ecovolt.mapping.dto.devicedto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DeviceRequestDto {

    /*
     * REGLA DE NEGOCIO: Identificador único de hardware (MAC/UUID).
     * No puede cambiar una vez registrado para mantener la integridad de la telemetría.
     */
    @NotBlank(message = "El número de serie es obligatorio")
    @Size(max = 100)
    private String serialNumber;

    @NotBlank(message = "El nombre del dispositivo es obligatorio")
    @Size(max = 100)
    private String name;

    /*
     * REGLA DE NEGOCIO: Categorización vía DataCatalogo.
     * Recibimos el ID numérico del diccionario (Ej: Iluminación, IT, Climatización).
     */
    @NotNull(message = "El ID de la categoría es obligatorio")
    private Integer categoryId;

    private String manufacturer;

    private String firmwareVersion;

    @NotNull(message = "El ID del ambiente (Room) es obligatorio")
    private Long roomId;
}