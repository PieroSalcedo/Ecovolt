package upc.ecovolt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import upc.ecovolt.entity.Device;

public interface DeviceRepository extends JpaRepository<Device,Long> {
}
