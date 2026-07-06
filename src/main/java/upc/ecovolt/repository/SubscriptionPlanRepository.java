package upc.ecovolt.repository;

import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import upc.ecovolt.entity.SubscriptionPlan;

public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan, Integer> {

    /*
     * REGLA DE NEGOCIO: Segmentación por Valor.
     * Lista planes en un rango de precio.
     * Ordenado por precio para mostrar una progresión lógica en el Frontend.
     */
    @Query("select p from SubscriptionPlan p where p.monthlyPrice between ?1 and ?2 and p.status = 1 order by p.monthlyPrice asc")
    List<SubscriptionPlan> findPlansByPriceRange(BigDecimal min, BigDecimal max);

    /*
     * REGLA DE NEGOCIO: Nivel de Servicio (SLA).
     * Filtra planes por el nivel de soporte (DataCatalog).
     */
    @Query("select p from SubscriptionPlan p where p.supportLevel.description = ?1 and p.status = 1")
    List<SubscriptionPlan> findBySupportLevelName(String supportLevel);

    /*
     * REGLA DE NEGOCIO: Análisis de Rentabilidad.
     * Cuenta usuarios activos vinculados a un plan específico.
     */
    @Query("select count(u) from User u where u.subscriptionPlan.idPlan = ?1 and u.status = 1")
    long countActiveUsersByPlan(Integer idPlan);

    /*
     * REGLA DE NEGOCIO: Estrategia de Upselling.
     * Sugiere planes con mayor límite de dispositivos.
     * Se usa cuando el usuario intenta agregar un equipo y ha superado su cuota.
     */
    @Query("select p from SubscriptionPlan p where p.deviceLimit > ?1 and p.status = 1 order by p.deviceLimit asc")
    List<SubscriptionPlan> findUpgradeOptions(Integer currentLimit);

    /*
     * REGLA DE NEGOCIO: Validación de Cuota (SaaS).
     * Obtiene solo el límite de dispositivos para validaciones rápidas en el Service.
     */
    @Query("select p.deviceLimit from SubscriptionPlan p where p.idPlan = ?1")
    Optional<Integer> getDeviceLimitById(Integer idPlan);

    /*
     * REGLA DE NEGOCIO: Consistencia de Catálogo.
     * Busca un plan por su nombre exacto.
     */
    @Query("select p from SubscriptionPlan p where p.name = ?1 and p.status = 1")
    Optional<SubscriptionPlan> findByName(String name);

    /*
     * REGLA DE NEGOCIO: Catálogo Público.
     * Lista todos los planes disponibles para nuevos registros.
     */
    @Query("select p from SubscriptionPlan p where p.status = 1 order by p.monthlyPrice asc")
    List<SubscriptionPlan> findAllActivePlans();
}