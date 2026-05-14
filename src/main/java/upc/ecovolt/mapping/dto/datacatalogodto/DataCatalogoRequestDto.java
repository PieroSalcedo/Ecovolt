package upc.ecovolt.mapping.dto.datacatalogodto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DataCatalogoRequestDto {
    @NotBlank(message = "La descripción de la opción es obligatoria")
    private String description;

    @NotNull(message = "El ID del catálogo maestro es obligatorio")
    private Integer idCatalog;
}