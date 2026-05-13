package upc.ecovolt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import upc.ecovolt.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification,Long> {
}
