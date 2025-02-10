package umc.GrowIT.Server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import umc.GrowIT.Server.domain.Keyword;

import java.util.Optional;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {
    Optional<Keyword> findByName(String name);

    @Query("SELECT K FROM Keyword K ORDER BY RAND() LIMIT 1")
    Keyword findRandomKeyword();
}
