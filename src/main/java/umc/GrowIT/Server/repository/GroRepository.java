package umc.GrowIT.Server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import umc.GrowIT.Server.domain.Gro;

import java.util.Optional;

public interface GroRepository extends JpaRepository<Gro, Long> {
    Optional<Gro> findByName(String name);

    boolean existsByName(@Param("name")String nickname);

    Optional<Gro> findByUserId(Long userId);

    boolean existsByUserId(Long userId);

    @Modifying
    @Query("DELETE FROM Gro g WHERE g.user.id = :userId")
    void deleteByUserId(@Param("userId") Long userId);

    // TODO 회원탈퇴 API 네이티브 삭제 메소드 - 추후 확인 필요
//    @Modifying
//    @Query(value = "DELETE FROM gro WHERE user_id = :userId", nativeQuery = true)
//    void deleteByUserIdNative(@Param("userId") Long userId);
}
