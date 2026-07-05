package upc.ecovolt.repository;

import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import upc.ecovolt.entity.SubscriptionPlan;

@Repository
public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan, Integer> {

    /*
     * REGLA DE NEGOCIO: Segmentación por Valor (Marketing).
     * Permite identificar planes en rangos de precio para campañas promocionales.
     */
    @Query("select p from SubscriptionPlan p where p.monthlyPrice between ?1 and ?2 and p.status = 1")
    List<SubscriptionPlan> findPlansByPriceRange(BigDecimal min, BigDecimal max);

    /*
     * REGLA DE NEGOCIO: Nivel de Servicio (SLA).
     * Filtra planes por la descripción del DataCatalogo (Ej: 'Premium').
     * Cruza la tabla SubscriptionPlan con DataCatalogo.
     */
    @Query("select p from SubscriptionPlan p where p.supportLevel.description = ?1 and p.status = 1")
    List<SubscriptionPlan> findBySupportLevelName(String supportLevel);

    /*
     * REGLA DE NEGOCIO: Análisis de Churn y Popularidad.
     * Cuenta cuántos usuarios reales están usando un plan para medir su rentabilidad.
     * Relación: User -> SubscriptionPlan.
     */
    @Query("select count(u) from User u where u.subscriptionPlan.idPlan = ?1 and u.status = 1")
    long countActiveUsersByPlan(Integer planId);

    /*
     * REGLA DE NEGOCIO: Estrategia de Upselling (Crecimiento).
     * Busca planes con mayor capacidad de dispositivos que el actual.
     * Se usa para sugerir una mejora de cuenta cuando el usuario llega a su límite.
     */
    @Query("select p from SubscriptionPlan p where p.deviceLimit > ?1 and p.status = 1 order by p.deviceLimit asc")
    List<SubscriptionPlan> findUpgradeOptions(Integer currentLimit);

    /*
     * REGLA DE NEGOCIO: Validación de Cuota (SaaS Rule).
     * Obtiene directamente el límite de dispositivos de un plan específico.
     * Optimiza el rendimiento al no traer toda la entidad.
     */
    @Query("select p.deviceLimit from SubscriptionPlan p where p.idPlan = ?1")
    Integer getDeviceLimitById(Integer planId);

    /*
     * REGLA DE NEGOCIO: Consistencia de Catálogo.
     * Busca por nombre único (Ej: 'EcoVolt Business Plus').
     */
    @Query("select p from SubscriptionPlan p where p.name = ?1")
    Optional<SubscriptionPlan> findByName(String name);
}