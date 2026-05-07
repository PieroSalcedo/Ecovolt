package upc.ecovolt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import upc.ecovolt.entity.Home;

public interface HomeRepository extends JpaRepository<Home,Long> {
}
