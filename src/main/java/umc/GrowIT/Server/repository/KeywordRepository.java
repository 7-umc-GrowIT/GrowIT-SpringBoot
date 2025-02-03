package umc.GrowIT.Server.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import umc.GrowIT.Server.domain.Keyword;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {
}

