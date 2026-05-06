package upc.ecovolt.service;

import upc.ecovolt.mapping.dto.userDto.UserRequestDto;
import upc.ecovolt.mapping.dto.userDto.UserResponseDto;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<UserResponseDto> findAllUsers();
    Optional<UserResponseDto> findUserById(Long id);
    UserResponseDto saveUser(UserRequestDto objUserRequestDto);
    UserResponseDto updateUser(Long id, UserRequestDto objUserRequestDto);
    void delete (Long id);
}
