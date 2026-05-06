package upc.ecovolt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import upc.ecovolt.entity.Room;

public interface RoomRepository extends JpaRepository<Room,Long> {
}
