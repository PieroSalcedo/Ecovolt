package upc.ecovolt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import upc.ecovolt.entity.Automation;

public interface AutomationRepository extends JpaRepository<Automation,Long> {
}
