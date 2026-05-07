package upc.ecovolt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import upc.ecovolt.entity.EnergyReading;

public interface EnergyReadingRepository extends JpaRepository<EnergyReading,Long> {
}
