package upc.ecovolt.service;

import upc.ecovolt.entity.Option;
import upc.ecovolt.entity.Role;
import upc.ecovolt.mapping.dto.userdto.UserRequestDto;
import upc.ecovolt.mapping.dto.userdto.UserResponseDto;

import java.util.List;
import java.util.Optional;

public interface UserService {
    // CRUD Básico
    List<UserResponseDto> findAllUsers();
    Optional<UserResponseDto> findUserById(Long id);
    UserResponseDto saveUser(UserRequestDto requestDto);
    UserResponseDto updateUser(Long id, UserRequestDto requestDto);
    void delete(Long id);

    // REGLAS DE SEGURIDAD Y NEGOCIO (Provenientes del Repositorio)
    Optional<UserResponseDto> findByLogin(String login);
    List<Option> traerEnlacesDeUsuario(Long idUser);
    List<Role> traerRolesDeUsuario(Long idUser);
    Integer getDeviceLimitByUserId(Long idUser);
    long countUsersByCity(String city);
}