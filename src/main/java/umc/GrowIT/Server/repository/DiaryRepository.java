package umc.GrowIT.Server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import umc.GrowIT.Server.domain.Diary;
import umc.GrowIT.Server.domain.enums.DiaryStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DiaryRepository extends JpaRepository<Diary, Long> {

    Optional<Diary> findByIdAndUserId(@Param("diaryId") Long diaryId, @Param("userId") Long userId);

    @Query("SELECT d FROM Diary d WHERE d.user.id = :userId AND YEAR(d.date) = :year AND MONTH(d.date) = :month " +
            "AND d.status = umc.GrowIT.Server.domain.enums.DiaryStatus.COMPLETED " +
            "ORDER BY d.date DESC")
    List<Diary> findByUserIdAndYearAndMonth(@Param("userId") Long userId,
                                            @Param("year") Integer year,
                                            @Param("month") Integer month);

    @Query("SELECT d FROM Diary d " +
            "WHERE d.user.id = :userId " +
            "AND d.id = :diaryId " +
            "AND d.status = umc.GrowIT.Server.domain.enums.DiaryStatus.COMPLETED")
    Optional<Diary> findCompletedByUserIdAndId(@Param("userId") Long userId,
                                               @Param("diaryId") Long diaryId);

    @Query("SELECT MAX(d.date) FROM Diary d WHERE d.user.id = :userId") // 마지막 일기 작성 날짜 조회
    Optional<LocalDate> findLastDiaryDateByUserId(@Param("userId") Long userId);

    // 오늘 작성한 일기 조회
    @Query("SELECT d FROM Diary d WHERE d.user.id = :userId AND d.date = :date")
    Optional<Diary> findTodayDiaryByUserId(@Param("userId") Long userId, @Param("date") LocalDate date);

    // 일기 상태 변경
    @Query("select d from Diary d where d.user.id = :userId and d.id = :diaryId and d.status = :diaryStatus")
    Optional<Diary> findByUserIdAndDiaryIdAndStatusForUpdate(@Param("userId") Long userId,
                                                          @Param("diaryId") Long diaryId,
                                                          @Param("diaryStatus") DiaryStatus diaryStatus);

    @Modifying
    @Query("DELETE FROM Diary d WHERE d.user.id = :userId")
    void deleteByUserId(@Param("userId") Long userId);

    boolean existsByUserIdAndDateAndStatus(Long userId, LocalDate date, DiaryStatus diaryStatus);

    @Modifying
    @Query("DELETE FROM Diary d WHERE d.status = :status")
    int deleteByStatus(@Param("status") DiaryStatus status);

    // TODO 회원탈퇴 API 네이티브 삭제 메소드 - 추후 확인 필요
//    @Modifying
//    @Query(value = "DELETE FROM diary WHERE user_id = :userId", nativeQuery = true)
//    void deleteByUserIdNative(@Param("userId") Long userId);
}
