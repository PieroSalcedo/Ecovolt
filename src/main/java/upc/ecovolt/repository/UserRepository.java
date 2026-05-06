package upc.ecovolt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import upc.ecovolt.entity.User;

public interface UserRepository extends JpaRepository<User,Long> {
}
