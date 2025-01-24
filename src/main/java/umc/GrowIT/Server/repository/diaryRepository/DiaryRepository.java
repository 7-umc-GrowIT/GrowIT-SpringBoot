package umc.GrowIT.Server.repository.diaryRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import umc.GrowIT.Server.domain.Diary;
import umc.GrowIT.Server.domain.Item;
import umc.GrowIT.Server.domain.enums.ItemCategory;

import java.util.List;
import java.util.Optional;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
    @Query("SELECT d FROM Diary d WHERE d.user.id = :userId AND YEAR(d.date) = :year AND MONTH(d.date) = :month")
    List<Diary> findByUserIdAndYearAndMonth(@Param("userId") Long userId,
                                            @Param("year") Integer year,
                                            @Param("month") Integer month);

    Optional<Diary> findByUserIdAndId(Long userId, Long diaryId);
}
