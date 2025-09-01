package umc.GrowIT.Server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import umc.GrowIT.Server.domain.DiaryKeyword;

public interface DiaryKeywordRepository extends JpaRepository<DiaryKeyword, Long> {


    @Modifying
    @Query("DELETE FROM DiaryKeyword dk WHERE dk.diary.user.id = :userId")
    void deleteByUserId(@Param("userId") Long userId);

    // TODO 회원탈퇴 API 네이티브 삭제 메소드 - 추후 확인 필요
//    @Modifying
//    @Query(value = """
//        DELETE dek FROM diary_keyword dek
//        INNER JOIN diary d ON dek.diary_id = d.id
//        WHERE d.user_id = :userId
//        """, nativeQuery = true)
//    void deleteByUserIdNative(@Param("userId") Long userId);
}
