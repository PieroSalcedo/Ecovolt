package upc.ecovolt.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import upc.ecovolt.entity.User;
import upc.ecovolt.entity.Role;
import upc.ecovolt.entity.Option;

public interface UserRepository extends JpaRepository<User, Long> {

    /*
     * REGLA DE SEGURIDAD: Autenticación.
     * Busca al usuario por su login para el proceso de inicio de sesión.
     */
    @Query("select u from User u where u.login = ?1 and u.status = 1")
    Optional<User> findByLogin(String login);

    /*
     * REGLA DE SEGURIDAD: Autorización (Roles).
     * Obtiene los roles del usuario cruzando con la tabla intermedia UserHasRol.
     * Nota: Se usa 'userHasRolPk' según la definición en tu entidad UserHasRol.
     */
    @Query("select r from Role r join UserHasRol ur on r.idRole = ur.userHasRolPk.idRol where ur.userHasRolPk.idUser = ?1")
    List<Role> findRolesByUserId(Long idUser);

    /*
     * REGLA DE SEGURIDAD DINÁMICA: Navegación (Enlaces).
     * Cruza las tablas de permisos para obtener los menús permitidos del usuario.
     * Ruta: User -> UserHasRol -> RoleHasOption -> Option.
     */
    @Query("select distinct o from Option o " +
            "join RoleHasOption ro on o.idOption = ro.id.idOption " +
            "join UserHasRol ur on ro.id.idRole = ur.userHasRolPk.idRol " +
            "where ur.userHasRolPk.idUser = ?1 and o.status = 1")
    List<Option> findNavOptionsByUserId(Long idUser);

    /*
     * REGLA DE NEGOCIO SaaS: Validación de Cuota.
     * Obtiene el límite de dispositivos del plan asignado al usuario.
     * Evita cargar toda la entidad User, mejorando el rendimiento.
     */
    @Query("select u.subscriptionPlan.deviceLimit from User u where u.idUser = ?1")
    Optional<Integer> getDeviceLimitByUserId(Long idUser);

    /*
     * REGLA DE NEGOCIO: Análisis Regional.
     * Cuenta usuarios únicos en una ciudad específica.
     */
    @Query("select count(distinct u) from User u join Home h on h.user.idUser = u.idUser " +
            "where h.city = ?1 and u.status = 1")
    long countUsersByCity(String city);

    /*
     * REGLA DE NEGOCIO: Integridad de Datos.
     * Valida si un correo ya está registrado (Case Insensitive).
     */
    @Query("select u from User u where lower(u.email) = lower(?1)")
    Optional<User> findByEmail(String email);

    /*
     * REGLA DE NEGOCIO: Validación de Registro.
     */
    @Query("select case when count(u) > 0 then true else false end from User u where u.login = ?1")
    boolean existsByLogin(String login);
}