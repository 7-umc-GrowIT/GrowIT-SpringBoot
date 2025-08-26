package umc.GrowIT.Server.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import umc.GrowIT.Server.domain.OAuthAccount;

import java.util.List;
import java.util.Optional;

public interface OAuthAccountRepository extends JpaRepository<OAuthAccount, Long> {
    @EntityGraph(attributePaths = {"user"})
    Optional<OAuthAccount> findByProviderId(Long providerId);

    boolean existsByProviderId(Long providerId);

    boolean existsByEmail(String email);

    @EntityGraph(attributePaths = {"user"})
    List<OAuthAccount> findByEmail(String email);

    @Query("SELECT oa FROM OAuthAccount oa WHERE oa.user.id = :userId")
    List<OAuthAccount> findOAuthAccountsByUserId(@Param("userId") Long userId);

    @Modifying
    @Query("DELETE FROM OAuthAccount oa WHERE oa.user.id = :userId")
    void deleteByUserId(@Param("userId") Long userId);

    // TODO 회원탈퇴 API 네이티브 삭제 메소드 - 추후 확인 필요
//    @Modifying
//    @Query(value = "DELETE FROM oauth_account WHERE user_id = :userId", nativeQuery = true)
//    void deleteByUserIdNative(@Param("userId") Long userId);
}
