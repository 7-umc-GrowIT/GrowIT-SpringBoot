package umc.GrowIT.Server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import umc.GrowIT.Server.domain.RefreshToken;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByRefreshToken(String refreshToken);

    @Modifying
    @Query(value = "DELETE FROM refresh_token WHERE id = :refreshTokenId", nativeQuery = true)
    void deleteByIdNative(@Param("refreshTokenId") Long refreshTokenId);

    // TODO soft delete 방식일 때, 스케줄러에서 사용하던 코드로 추후 확인 필요
//    @Modifying
//    @Query("DELETE FROM RefreshToken rt WHERE rt.id IN :ids")
//    void deleteByIds(@Param("ids") List<Long> ids);
}
