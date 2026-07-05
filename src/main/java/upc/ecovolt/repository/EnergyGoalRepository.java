package upc.ecovolt.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import upc.ecovolt.entity.EnergyGoal;

public interface EnergyGoalRepository extends JpaRepository<EnergyGoal, Integer> {

    /*
     * REGLA DE NEGOCIO: Monitoreo de Metas por Propiedad.
     * Busca las metas de ahorro según su estado para una casa específica.
     * El Frontend lo usará para mostrar el progreso de ahorro en una casa.
     */
    List<EnergyGoal> findByHome_IdHomeAndStatus(Long idHome, Integer status);

    /*
     * REGLA DE NEGOCIO: Alerta Preventiva / Sistema de Notificaciones.
     * Busca metas que superen o igualen un umbral crítico de consumo.
     * Útil para procesos en segundo plano que disparan notificaciones Push/Email.
     */
    List<EnergyGoal> findByAlertThresholdPercentageGreaterThanEqual(Integer threshold);

    /*
     * REGLA DE NEGOCIO: Vista Global del Usuario.
     * Obtiene todas las metas (activas o no) de todas las casas de un usuario.
     * Ruta: EnergyGoal -> Home -> User.
     */
    List<EnergyGoal> findByHome_User_IdUser(Long idUser);

    /*
     * REGLA DE NEGOCIO: Validación de Configuración.
     * Verifica si una casa ya tiene una meta con un límite mensual muy bajo.
     */
    boolean existsByHome_IdHomeAndStatus(Long idHome, Integer status);

    // Buscar meta activa por Vivienda
    @Query("select g from EnergyGoal g where g.home.idHome = ?1 and g.status = 1")
    Optional<EnergyGoal> findActiveByHome(Long idHome);

    // Buscar meta activa por Cuarto
    @Query("select g from EnergyGoal g where g.room.idRoom = ?1 and g.status = 1")
    Optional<EnergyGoal> findActiveByRoom(Long idRoom);

    // Buscar meta activa por Dispositivo
    @Query("select g from EnergyGoal g where g.device.idDevice = ?1 and g.status = 1")
    Optional<EnergyGoal> findActiveByDevice(Long idDevice);
}