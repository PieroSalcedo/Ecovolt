package upc.ecovolt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import upc.ecovolt.entity.EnergyReport;

public interface EnergyReportRepository extends JpaRepository<EnergyReport,Long> {
}
