package umc.GrowIT.Server.repository;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import umc.GrowIT.Server.domain.AuthenticationCode;
import umc.GrowIT.Server.domain.enums.CodeStatus;

import java.time.LocalDateTime;
import java.util.Optional;

public interface AuthenticationCodeRepository extends JpaRepository<AuthenticationCode, Integer> {
    // 특정 이메일의 expirationDate가 지금 이후 & status가 유효한 데이터 찾기
    Optional<AuthenticationCode> findByEmailAndExpirationDateAfterAndStatus(String email, LocalDateTime currentDateTime, CodeStatus codeStatus);
}
