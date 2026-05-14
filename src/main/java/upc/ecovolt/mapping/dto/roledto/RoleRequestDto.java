package upc.ecovolt.mapping.dto.roledto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RoleRequestDto {

    /*
     * REGLA DE NEGOCIO: Prefijo estándar de Spring Security.
     * Los nombres deben seguir el formato 'ROLE_NOMBRE' (Ej: ROLE_ADMIN).
     */
    @NotBlank(message = "El nombre del rol es obligatorio")
    @Size(max = 50)
    private String nombre;
}