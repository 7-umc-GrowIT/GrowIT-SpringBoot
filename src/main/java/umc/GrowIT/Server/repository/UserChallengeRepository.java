package umc.GrowIT.Server.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import umc.GrowIT.Server.domain.UserChallenge;
import umc.GrowIT.Server.domain.enums.UserChallengeType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserChallengeRepository extends JpaRepository<UserChallenge, Long> {

    // 특정 챌린지 ID와 매핑된 인증 내역 조회
    Optional<UserChallenge> findByIdAndUserId(@Param("challengeId") Long challengeId, @Param("userId") Long userId);

    // 오늘 날짜 기준으로 저장된 챌린지만 필터링
    @Query("SELECT uc FROM UserChallenge uc " +
            "WHERE uc.user.id = :userId " +
            "AND uc.date = :today")
    List<UserChallenge> findTodayUserChallengesByUserId(
            @Param("userId") Long userId,
            @Param("today") LocalDate today);

    // 전체 챌린지 중 인증 완료 여부 필터
    @Query("SELECT uc FROM UserChallenge uc " +
            "WHERE uc.user.id = :userId AND uc.completed = :completed " +
            "ORDER BY uc.createdAt DESC")
    Page<UserChallenge> findChallengesByCompletionStatus(@Param("userId") Long userId,
                                                         @Param("completed") Boolean completed,
                                                         Pageable pageable);

    // 챌린지 타입 + 인증 완료 여부 필터
    @Query("SELECT uc FROM UserChallenge uc " +
            "WHERE uc.user.id = :userId " +
            "AND uc.challengeType = :challengeType " +
            "AND uc.completed = :completed " +
            "ORDER BY uc.createdAt DESC")
    Page<UserChallenge> findByTypeAndCompletion(
            @Param("userId") Long userId,
            @Param("challengeType") UserChallengeType userChallengeType,
            @Param("completed") Boolean completed,
            Pageable pageable);

    //userId와 date로 UserChallenge 조회
    @Query("SELECT uc FROM UserChallenge uc " +
            "WHERE uc.user.id = :userId " +
            "AND uc.date = :date ")
    List<UserChallenge> findUserChallengesByDateAndUserId(
            @Param("userId") Long userId,
            @Param("date") LocalDate date
    );

    // 하루에 인증 완료한 챌린지 개수 조회
    @Query("SELECT COUNT(uc) FROM UserChallenge uc " +
            "WHERE uc.user.id = :userId " +
            "AND uc.completed = true " +
            "AND uc.createdAt BETWEEN :startOfDay AND :endOfDay")
    long countCompletedTodayByUserId(@Param("userId") Long userId,
                           @Param("startOfDay") LocalDateTime startOfDay,
                           @Param("endOfDay") LocalDateTime endOfDay);

    // 동일한 date에 대해 인증 완료한 챌린지 개수 조회 (크레딧 지급 용도)
    @Query("SELECT COUNT(uc) FROM UserChallenge uc " +
            "WHERE uc.user.id = :userId " +
            "AND uc.completed = true " +
            "AND uc.date = :date")
    long countCompletedOnDateByUserId(@Param("userId") Long userId, @Param("date") LocalDate date);

    @Modifying
    @Query("DELETE FROM UserChallenge uc WHERE uc.user.id = :userId")
    void deleteByUserId(@Param("userId") Long userId);

    // TODO 회원탈퇴 API 네이티브 삭제 메소드 - 추후 확인 필요
//    @Modifying
//    @Query(value = "DELETE FROM user_challenge WHERE user_id = :userId", nativeQuery = true)
//    void deleteByUserIdNative(@Param("userId") Long userId);
}




