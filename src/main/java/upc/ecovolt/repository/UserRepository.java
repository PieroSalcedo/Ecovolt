package upc.ecovolt.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import upc.ecovolt.entity.User;
import upc.ecovolt.entity.Role;
import upc.ecovolt.entity.Option;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /*
     * REGLA DE SEGURIDAD: Autenticación.
     * Busca al usuario por su login para el proceso de inicio de sesión.
     */
    @Query("select u from User u where u.login = ?1 and u.status = 1")
    Optional<User> findByLogin(String login);

    /*
     * REGLA DE SEGURIDAD DINÁMICA: Criterio del Profesor.
     * Cruza las tablas mediante las clases de ID embebido para obtener los menús permitidos.
     * Nota: 'ro.id' mapea a RoleHasOptionPK y 'ur.usuarioHasRolPk' a UsuarioHasRolPK.
     */
    @Query("SELECT o FROM Option o " +
            "JOIN RoleHasOption ro ON o.idOption = ro.id.idOption " +
            "JOIN UsuarioHasRol ur ON ro.id.idRol = ur.usuarioHasRolPk.idRol " +
            "WHERE ur.usuarioHasRolPk.idUser = ?1")
    List<Option> traerEnlacesDeUsuario(Long idUser);

    /*
     * REGLA DE SEGURIDAD: Autorización.
     * Obtiene los roles del usuario cruzando con la tabla intermedia.
     */
    @Query("SELECT r FROM Role r JOIN UsuarioHasRol ur ON r.idRol = ur.usuarioHasRolPk.idRol WHERE ur.usuarioHasRolPk.idUser = ?1")
    List<Role> traerRolesDeUsuario(Long idUser);

    /*
     * REGLA DE NEGOCIO SaaS: Validación de Cuota.
     * Obtiene el límite de dispositivos del plan asignado al usuario.
     * Optimiza el rendimiento al devolver solo el valor numérico (Integer).
     */
    @Query("select u.subscriptionPlan.deviceLimit from User u where u.id = ?1")
    Integer getDeviceLimitByUserId(Long idUser);

    /*
     * REGLA DE NEGOCIO: Análisis Regional (DEVIDA).
     * Cuenta usuarios únicos en una ciudad específica uniendo User con Home.
     */
    @Query("select count(distinct u) from User u, Home h " +
            "where h.user.id = u.id and h.city = ?1 and u.status = 1")
    long countUsersByCity(String city);

    /*
     * REGLA DE NEGOCIO: Integridad.
     * Valida existencia de correo electrónico para el proceso de registro.
     */
    @Query("select u from User u where u.email = ?1")
    Optional<User> findByEmail(String email);
}