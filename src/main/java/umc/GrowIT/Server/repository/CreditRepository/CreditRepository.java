package umc.GrowIT.Server.repository.CreditRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.GrowIT.Server.domain.User;

import java.util.Optional;

public interface CreditRepository extends JpaRepository<User, Long>, CreditRepositoryCustom {

    //현재 보유 크레딧 조회
    Integer findCurrentCreditById(Long id);

    //누적 크레딧 조회
    Integer findTotalCreditById(Long creditId);
}
