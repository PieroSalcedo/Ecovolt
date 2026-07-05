package upc.ecovolt.service;

import org.springframework.security.access.prepost.PreAuthorize;
import upc.ecovolt.entity.Option;
import upc.ecovolt.entity.Role;
import upc.ecovolt.mapping.dto.UserDto;

import java.util.List;
import java.util.Optional;

public interface UserService {

    @PreAuthorize("hasAnyRole('ADMIN', 'AUDITOR')")
    List<UserDto.Response> findAllUsers();

    // Regla Especial: El Admin entra, o el usuario cuyo ID coincida con el autenticado
    @PreAuthorize("hasRole('ADMIN') or #id == principal.idUser")
    Optional<UserDto.Response> findUserById(Long id);

    @PreAuthorize("permitAll()") // Registro libre
    UserDto.Response saveUser(UserDto.Request requestDto);

    @PreAuthorize("hasRole('ADMIN') or #id == principal.idUser")
    UserDto.Response updateUser(Long id, UserDto.Request requestDto);

    @PreAuthorize("hasRole('ADMIN')")
    void delete(Long id);

    // --- REGLAS DE NEGOCIO ---

    @PreAuthorize("hasRole('ADMIN')")
    Optional<UserDto.Response> findByLogin(String login);

    @PreAuthorize("isAuthenticated()") // Cualquiera logueado puede ver su propio menú
    List<Option> traerEnlacesDeUsuario(Long idUser);

    @PreAuthorize("hasRole('ADMIN')") // Ver roles es una tarea administrativa
    List<Role> traerRolesDeUsuario(Long idUser);

    @PreAuthorize("isAuthenticated()")
    Integer getDeviceLimitByUserId(Long idUser);

    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST')")
    long countUsersByCity(String city);
}
