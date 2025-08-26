package umc.GrowIT.Server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import umc.GrowIT.Server.domain.UserTerm;

import java.util.Optional;

public interface UserTermRepository extends JpaRepository<UserTerm, Long>{
    Optional<UserTerm> findByTermId(Long termId);


    @Modifying
    @Query("DELETE FROM UserTerm ut WHERE ut.user.id = :userId")
    void deleteByUserId(@Param("userId") Long userId);

    // TODO 회원탈퇴 API 네이티브 삭제 메소드 - 추후 확인 필요
//    @Modifying
//    @Query(value = "DELETE FROM user_term WHERE user_id = :userId", nativeQuery = true)
//    void deleteByUserIdNative(@Param("userId") Long userId);
}
