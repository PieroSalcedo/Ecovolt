package upc.ecovolt.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import upc.ecovolt.entity.Option;

public interface OptionRepository extends JpaRepository<Option, Integer> {

    /*
     * REGLA DE NEGOCIO: Construcción Dinámica de Interfaz.
     * Filtra por tipo (1: Menú, 2: Botón, 3: Config).
     * El Frontend lo usa para renderizar la barra lateral (Sidebar).
     */
    @Query("select o from Option o where o.type = ?1 and o.status = 1 order by o.name asc")
    List<Option> findByType(Integer type);

    /*
     * REGLA DE NEGOCIO: Disponibilidad de Funciones.
     * Lista todas las opciones operativas. Útil para el gestor de permisos.
     */
    @Query("select o from Option o where o.status = 1")
    List<Option> findActiveOptions();

    /*
     * REGLA DE NEGOCIO: Seguridad de Navegación.
     * Busca una opción por su ruta (Ej: '/reports/energy').
     * Devuelve Optional para manejar rutas no existentes de forma segura.
     */
    @Query("select o from Option o where o.route = ?1 and o.status = 1")
    Optional<Option> findByRoute(String route);

    /*
     * REGLA DE NEGOCIO: Control de Acceso Basado en Roles (RBAC).
     * Esta es la consulta CLAVE para el Frontend.
     * Devuelve qué menús puede ver un usuario según su ID de Rol.
     * Navega: RoleHasOption -> Option.
     */
    @Query("select rho.option from RoleHasOption rho where rho.role.idRole = ?1 and rho.option.status = 1")
    List<Option> findOptionsByRoleId(Integer idRole);

    /*
     * REGLA DE NEGOCIO: Identificación por Nombre Único.
     * Busca la opción por su nombre lógico (Ej: 'DASHBOARD_VIEW').
     */
    @Query("select o from Option o where o.name = ?1 and o.status = 1")
    Optional<Option> findByName(String name);
}