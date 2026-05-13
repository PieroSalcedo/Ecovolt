package upc.ecovolt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import upc.ecovolt.entity.Role;

public interface RoleRepository extends JpaRepository<Role,Long> {
}
