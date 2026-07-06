package upc.ecovolt.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import upc.ecovolt.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    /*
     * REGLA DE NEGOCIO: Asignación de Perfiles y Seguridad.
     * Busca el rol por su identificador único (Ej: 'ROLE_ADMIN').
     * Se usa en el proceso de Login para cargar las autoridades del usuario.
     */
    @Query("select r from Role r where r.name = ?1 and r.status = 1")
    Optional<Role> findByName(String name);

    /*
     * REGLA DE NEGOCIO: Gestión Administrativa.
     * Lista los roles habilitados para ser asignados en el panel de usuarios.
     * Ordenado alfabéticamente para facilitar la selección en el Frontend.
     */
    @Query("select r from Role r where r.status = 1 order by r.name asc")
    List<Role> findActiveRoles();

    /*
     * REGLA DE NEGOCIO: Análisis de Usuarios por Perfil.
     * Cuenta cuántos usuarios tienen asignado un rol (Ej: ¿Cuántos Clientes hay?).
     * Realiza un JOIN entre User y sus Roles.
     */
    @Query("select count(u) from User u join u.roles r where r.idRole = ?1")
    long countUsersByRoleId(Integer idRole);

    /*
     * REGLA DE NEGOCIO: Validación de Integridad.
     * Verifica si un rol existe antes de intentar asignarlo.
     */
    @Query("select case when count(r) > 0 then true else false end from Role r where r.name = ?1")
    boolean existsByName(String name);
}