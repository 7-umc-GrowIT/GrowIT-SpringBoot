package umc.GrowIT.Server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import umc.GrowIT.Server.domain.User;
import umc.GrowIT.Server.domain.enums.UserStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>  {

    Optional<User> findByPrimaryEmail(String primaryEmail);

    boolean existsByPrimaryEmail(String primaryEmail);

    @Query("SELECT user.password FROM User user WHERE user.primaryEmail = :primaryEmail")
    String findPasswordByPrimaryEmail(@Param("primaryEmail") String primaryEmail);

    @Modifying
    @Query(value = "DELETE FROM user WHERE id = :userId", nativeQuery = true)
    void deleteByIdNative(@Param("userId") Long userId);


    // TODO soft delete 방식일 때, 스케줄러에서 사용하던 코드로 추후 확인 필요
//    // status가 inactive이고, updated_at이 30일 지난 사용자 삭제
//    @Modifying
//    @Query("DELETE FROM User u WHERE u.status = 'inactive' AND u.updatedAt <= :threshold")
//    int deleteByStatusAndUpdatedAtBefore(@Param("threshold") LocalDateTime threshold);
//
//    @Query("SELECT u.refreshToken.id FROM User u WHERE u.status = 'INACTIVE' AND u.updatedAt < :threshold")
//    List<Long> findRefreshTokenIdsForInactiveUsers(@Param("threshold") LocalDateTime threshold);
}