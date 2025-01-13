package umc.GrowIT.Server.repository.CreditRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import umc.GrowIT.Server.domain.User;

import java.util.Optional;

public interface CreditRepository extends JpaRepository<User, Long>, CreditRepositoryCustom {

    // 현재 보유 크레딧 조회
//    public Integer findCurrentCreditById(Long id);

    // 누적 크레딧 조회
//    public Integer findTotalCreditById(Long id);

    @Query("SELECT u.currentCredit FROM User u WHERE u.id = :id")
    Integer findCurrentCreditById(@Param("id") Long id);

    @Query("SELECT u.totalCredit FROM User u WHERE u.id = :id")
    Integer findTotalCreditById(@Param("id") Long id);
}
