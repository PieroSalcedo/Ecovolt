package upc.ecovolt.mapping.dto.optiondto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OptionRequestDto {

    @NotBlank(message = "El nombre de la opción es obligatorio")
    private String nombre;

    @NotBlank(message = "La ruta de navegación es obligatoria")
    private String ruta;

    /*
     * REGLA DE NEGOCIO: Clasificación de UI.
     * 1: Menú Lateral, 2: Botón de Acción, 3: Ajustes.
     */
    @NotNull(message = "El tipo de opción es obligatorio")
    private Integer tipo;
}