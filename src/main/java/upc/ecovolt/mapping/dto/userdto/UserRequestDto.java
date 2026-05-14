package upc.ecovolt.mapping.dto.userdto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserRequestDto {

    @NotBlank(message = "El nombre de usuario (login) es obligatorio")
    @Size(max = 50)
    private String login; // REGLA DE SEGURIDAD: Necesario para el Auth

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100)
    private String firstName;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 100)
    private String lastName;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Formato de email inválido")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    @NotNull(message = "El ID del plan es obligatorio")
    private Integer subscriptionPlanId; // Cambiado a Integer para coincidir con la entidad
}