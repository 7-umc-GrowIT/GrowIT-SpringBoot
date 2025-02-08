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

    @Query("SELECT MAX(d.date) FROM Diary d WHERE d.user.id = :userId") // 마지막 일기 작성 날짜 조회
    Optional<LocalDate> findLastDiaryDateByUserId(@Param("userId") Long userId);

    // 오늘 작성한 일기 조회
    @Query("SELECT d FROM Diary d WHERE d.user.id = :userId AND d.date = :today")
    Optional<Diary> findTodayDiaryByUserId(@Param("userId") Long userId, @Param("today") LocalDate today);
}
