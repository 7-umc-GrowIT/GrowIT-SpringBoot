package umc.GrowIT.Server.scheduler;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import umc.GrowIT.Server.domain.AuthenticationCode;
import umc.GrowIT.Server.repository.AuthenticationCodeRepository;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class Scheduler {
    private final AuthenticationCodeRepository authenticationCodeRepository;

    // 인증번호 삭제 스케줄러
    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정 실행
    @Transactional
    public void deleteExpiredAuthenticationCodes() {
        try {
            // 기준일
            int expirationDays = 14;

            LocalDateTime threshold = LocalDateTime.now().minusDays(expirationDays);
            int count = authenticationCodeRepository.deleteByExpirationDateBefore(threshold);
            log.info("[스케줄러 : 만료시간이" + expirationDays + "일 지난 데이터 삭제");
            log.info("[스케줄러] : 삭제된 데이터 개수 : " + count);
        } catch (Exception e) {
            log.error("[스케줄러] : 인증번호 삭제 실패", e);
        }
    }
}
