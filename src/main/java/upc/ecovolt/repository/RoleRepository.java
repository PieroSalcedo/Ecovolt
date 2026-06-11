package upc.ecovolt.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import upc.ecovolt.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    /*
     * REGLA DE NEGOCIO: Asignación de Perfiles.
     * Busca un rol por su nombre único (Ej: 'ROLE_CUSTOMER').
     * Es vital durante el registro de nuevos usuarios para asignar el rol por defecto.
     */
    @Query("select r from Role r where r.name = ?1")
    Optional<Role> findByName(String name);

    /*
     * REGLA DE NEGOCIO: Auditoría de Seguridad.
     * Lista solo los roles que están habilitados en el sistema.
     */
    @Query("select r from Role r where r.status = 1")
    List<Role> findActiveRoles();

    /*
     * REGLA DE NEGOCIO: Análisis de Usuarios por Perfil.
     * Cuenta cuántos usuarios tienen asignado un rol específico.
     * Cruce: Role -> User (a través de la relación ManyToMany).
     */
    @Query("select count(u) from User u join u.roles r where r.idRole = ?1")
    long countUsersByRoleId(Integer idRole);
}
