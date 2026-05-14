package upc.ecovolt.mapping.dto.catalogodto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CatalogoRequestDto {
    @NotBlank(message = "La descripción del catálogo es obligatoria")
    private String description;
}