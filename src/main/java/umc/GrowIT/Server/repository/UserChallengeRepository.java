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


    // 1. 유저의 완료 또는 미완료 챌린지 조회 (dtype 무시)
    @Query("SELECT uc FROM UserChallenge uc " +
            "WHERE uc.user.id = :userId " +
            "AND uc.completed = :completed")
    Page<UserChallenge> findChallengesByCompletionStatus(
            @Param("userId") Long userId,
            @Param("completed") Boolean completed,
            Pageable pageable);

    // 2. 특정 dtype에 대해 미완료 챌린지 조회 (completed가 true인 챌린지는 제외)
    @Query("SELECT uc FROM UserChallenge uc " +
            "WHERE uc.user.id = :userId " +
            "AND uc.dtype = :dtype " +
            "AND uc.completed = false")  // 항상 미완료 챌린지만 조회
    Page<UserChallenge> findChallengesByDtypeAndCompletionStatus(
            @Param("userId") Long userId,
            @Param("dtype") UserChallengeType dtype,
            Pageable pageable);

    //userId와 date로 UserChallenge 조회
    @Query("SELECT uc FROM UserChallenge uc " +
            "WHERE uc. user.id = :userId " +
            "AND uc.date = :date ")
    List<UserChallenge> findUserChallengesByDateAndUserId(
            @Param("userId") Long userId,
            @Param("date") LocalDate date
    );

}




