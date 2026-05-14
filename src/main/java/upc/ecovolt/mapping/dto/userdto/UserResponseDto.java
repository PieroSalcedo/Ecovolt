package upc.ecovolt.mapping.dto.userdto;

import lombok.Data;

@Data
public class UserResponseDto {
    private Long id;
    private String login;
    private String firstName;
    private String lastName;
    private String email;
    private Integer subscriptionPlanId;

    /* REGLA DE NEGOCIO: Mostrar información descriptiva al usuario */
    private String subscriptionPlanName;

    private Integer status;
}