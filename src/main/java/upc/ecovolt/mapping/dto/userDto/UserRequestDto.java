package upc.ecovolt.mapping.dto.userDto;

import lombok.Data;

//Esta clase DTO es lo que recibe el sistema
@Data
public class UserRequestDto {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    //como por ejemplo el password es necesario para crear la cuenta por ejemplo
}
