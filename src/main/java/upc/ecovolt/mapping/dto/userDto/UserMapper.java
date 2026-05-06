package upc.ecovolt.mapping.dto.userDto;
import org.mapstruct.Mapper;
import upc.ecovolt.entity.User;

import java.util.List;

@Mapper(componentModel = "spring") //Le dice a MapStruct que genere una clase de spring
public interface UserMapper {
    // 1. De Entidad a Respuesta (Para mostrar datos sin el password)
    UserResponseDto toDto(User user);

    // 2. De Solicitud a Entidad (Para recibir datos y guardarlos en la BD)
    User toEntity(UserRequestDto userRequestDto);

    // 3. Mapeo de listas (Muy útil para el método findAllUsers()
    List<UserResponseDto> toDtoList(List<User> userList);
}
