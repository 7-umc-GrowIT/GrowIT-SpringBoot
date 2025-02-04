package umc.GrowIT.Server.repository.diaryRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import umc.GrowIT.Server.domain.Diary;
import umc.GrowIT.Server.domain.Item;
import umc.GrowIT.Server.domain.enums.ItemCategory;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
    @Query("SELECT d FROM Diary d WHERE d.user.id = :userId AND YEAR(d.date) = :year AND MONTH(d.date) = :month")
    List<Diary> findByUserIdAndYearAndMonth(@Param("userId") Long userId,
                                            @Param("year") Integer year,
                                            @Param("month") Integer month);

    Optional<Diary> findByUserIdAndId(Long userId, Long diaryId);

    boolean existsByUserIdAndDate(Long userId, LocalDate date);

    @Query("SELECT MIN(d.date) FROM Diary d WHERE d.user.id = :userId") // 최초 일기 작성 날짜 조회
    Optional<LocalDate> findFirstDiaryDateByUserId(@Param("userId") Long userId);

}
