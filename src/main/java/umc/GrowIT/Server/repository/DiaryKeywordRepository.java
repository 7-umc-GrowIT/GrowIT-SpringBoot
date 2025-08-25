package umc.GrowIT.Server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import umc.GrowIT.Server.domain.DiaryKeyword;

public interface DiaryKeywordRepository extends JpaRepository<DiaryKeyword, Long> {
    @Modifying
    @Query(value = """
        DELETE dek FROM diary_keyword dek 
        INNER JOIN diary d ON dek.diary_id = d.id 
        WHERE d.user_id = :userId
        """, nativeQuery = true)
    void deleteByUserIdNative(@Param("userId") Long userId);
}
