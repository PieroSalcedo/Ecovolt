package upc.ecovolt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import upc.ecovolt.entity.SubscriptionPlan;

public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan,Long> {
}
