package umc.GrowIT.Server.repository.GroRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import umc.GrowIT.Server.domain.Gro;

import java.util.Optional;

public interface GroRepository extends JpaRepository<Gro, Long> {
    Optional<Gro> findByName(String name);

    boolean existsByName(@Param("name")String nickname);
}
