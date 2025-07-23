package umc.GrowIT.Server.repository.CreditRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import umc.GrowIT.Server.domain.User;

public interface UserCreditRepository extends JpaRepository<User, Long> {

    // 누적 크레딧 조회
//    public Integer findTotalCreditById(Long id);
    @Query("SELECT u.currentCredit FROM User u WHERE u.id = :id")
    Integer findCurrentCreditById(@Param("id") Long id);

    @Query("SELECT u.totalCredit FROM User u WHERE u.id = :id")
    Integer findTotalCreditById(@Param("id") Long id);

    @Modifying // 사용자의 크레딧 개수 업데이트
    @Query("UPDATE User u SET u.currentCredit = u.currentCredit + :creditAmount, u.totalCredit = u.totalCredit + :creditAmount WHERE u.id = :userId")
    void addCreditToUser(@Param("userId") Long userId, @Param("creditAmount") Integer creditAmount);
}
