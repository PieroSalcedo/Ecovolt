package upc.ecovolt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import upc.ecovolt.entity.Alert;

public interface AlertRepository extends JpaRepository<Alert, Long> {
}
