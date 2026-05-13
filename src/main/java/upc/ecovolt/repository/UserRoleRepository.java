package upc.ecovolt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import upc.ecovolt.entity.UserRole;

public interface UserRoleRepository extends JpaRepository<UserRole,Long> {
}
