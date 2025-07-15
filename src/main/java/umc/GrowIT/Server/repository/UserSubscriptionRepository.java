package umc.GrowIT.Server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import umc.GrowIT.Server.domain.UserSubscription;

@Repository
public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, Long>  {
    @Query("SELECT COUNT(us) > 0 FROM UserSubscription us " +
            "WHERE us.user.id = :userId " +
            "AND us.status = 'ACTIVE' " +
            "AND us.expiresAt > CURRENT_TIMESTAMP")
    boolean isUserActivelySubscribed(@Param("userId") Long userId);
}