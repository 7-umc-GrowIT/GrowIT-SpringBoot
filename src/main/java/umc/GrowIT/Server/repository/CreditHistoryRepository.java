package umc.GrowIT.Server.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import umc.GrowIT.Server.domain.CreditHistory;
import umc.GrowIT.Server.domain.User;
import umc.GrowIT.Server.domain.enums.CreditSource;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface CreditHistoryRepository extends JpaRepository<CreditHistory, Long> {
    boolean existsByUserAndDateAndSource(User user, LocalDate date, CreditSource creditSource);

    Slice<CreditHistory> findByUserAndCreatedAtBetweenOrderByCreatedAtDesc(
            User user, LocalDateTime start, LocalDateTime end, Pageable pageable);

    Slice<CreditHistory> findByUserAndCreatedAtBetweenAndAmountGreaterThanOrderByCreatedAtDesc(
            User user, LocalDateTime start, LocalDateTime end, int amount, Pageable pageable);

    Slice<CreditHistory> findByUserAndCreatedAtBetweenAndAmountLessThanOrderByCreatedAtDesc(
            User user, LocalDateTime start, LocalDateTime end, int amount, Pageable pageable);

    @Modifying
    @Query("DELETE FROM CreditHistory ch WHERE ch.user.id = :userId")
    void deleteByUserId(@Param("userId") Long userId);
}
