package upc.ecovolt.mapping.dto.userdto;

import lombok.Data;

//Esta clase DTO es lo que devuelve el sistema, por ejemplo para los get
@Data
public class UserResponseDto {
    private Long id; //El front necesita el id para saber quien es, pero no el dni
    private String email;
    private String firstName;
    private String lastName;
}
