package upc.ecovolt.mapping.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDto {
    @NotBlank(message = "El login no puede estar vacío")
    private String login;

    @NotBlank(message = "La contraseña no puede estar vacía")
    private String password;
}