package upc.ecovolt.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import upc.ecovolt.entity.EnergyGoal;

@Repository
public interface EnergyGoalRepository extends JpaRepository<EnergyGoal, Integer> {

    /*
     * REGLA DE NEGOCIO: Monitoreo de Metas.
     * Busca la meta de ahorro vigente para una casa específica.
     */
    @Query("select eg from EnergyGoal eg where eg.home.idHome = ?1 and eg.status = 1")
    List<EnergyGoal> findActiveGoalsByHome(Long idHome);

    /*
     * REGLA DE NEGOCIO: Alerta Preventiva.
     * Busca metas que tengan un umbral de alerta específico (Ej: 90%).
     */
    @Query("select eg from EnergyGoal eg where eg.alertThresholdPercentage >= ?1")
    List<EnergyGoal> findCriticalGoals(Integer threshold);
}