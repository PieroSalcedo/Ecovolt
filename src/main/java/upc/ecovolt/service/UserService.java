package upc.ecovolt.service;

import upc.ecovolt.mapping.dto.UserDto;
import upc.ecovolt.mapping.dto.OptionDto;
import upc.ecovolt.mapping.dto.RoleDto;

import java.util.List;
import java.util.Optional;

public interface UserService {

    void updateUserPlan(Long idUser, Integer idPlan);

    List<UserDto.Response> findAllUsers();

    Optional<UserDto.Response> findUserById(Long idUser);

    UserDto.Response saveUser(UserDto.Request requestDto);

    UserDto.Response updateUser(Long idUser, UserDto.Request requestDto);

    void delete(Long idUser);

    Optional<UserDto.Response> findByLogin(String login);

    /*
     * REGLA DE SEGURIDAD DINÁMICA:
     * Obtiene los menús (enlaces) permitidos para el usuario según sus roles.
     */
    List<OptionDto.Response> findNavOptionsByUserId(Long idUser);

    List<RoleDto.Response> findRolesByUserId(Long idUser);

    Integer getDeviceLimitByUserId(Long idUser);

    long countUsersByCity(String city);
}