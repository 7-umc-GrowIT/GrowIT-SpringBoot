package umc.GrowIT.Server.scheduler;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import umc.GrowIT.Server.domain.enums.DiaryStatus;
import umc.GrowIT.Server.repository.AuthenticationCodeRepository;
import umc.GrowIT.Server.repository.DiaryRepository;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class Scheduler {

    private final AuthenticationCodeRepository authenticationCodeRepository;
    private final DiaryRepository diaryRepository;

    // 인증번호 삭제 스케줄러
    @Transactional
    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정
    public void deleteExpiredAuthenticationCodes() {
        try {
            LocalDateTime now = LocalDateTime.now();

            int deletedCount = authenticationCodeRepository.deleteExpiredUnverifiedCodes(now);

            log.info("[스케줄러] : 인증번호 삭제 진행 (삭제된 데이터 개수 : {}개)", deletedCount);
        } catch (Exception e) {
            log.error("[스케줄러] : 인증번호 삭제 실패", e);
        }
    }

    // 일기 삭제 스케줄러
    @Transactional
    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정
    public void deletePendingDiaries() {
        try {
            int deletedCount = diaryRepository.deleteByStatus(DiaryStatus.PENDING);

            log.info("[스케줄러] : PENDING 상태 일기 삭제 진행 (삭제된 데이터 개수 : {}개)", deletedCount);
        } catch (Exception e) {
            log.error("[스케줄러] : PENDING 상태 일기 삭제 실패", e);
        }
    }

    // 회원탈퇴 삭제 스케줄러
//    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정 실행
//    @Transactional
//    public void deleteInactiveUsers() {
//        try {
//            // 기준일
//            int expirationDays = 30;
//
//            // 만료된 기준 날짜 계산 (현재 시간에서 30일 전)
//            LocalDateTime threshold = LocalDateTime.now().minusDays(expirationDays);
//
//            // 1. INACTIVE 상태이고 30일 이상 지난 사용자들의 RefreshToken ID 조회
//            List<Long> refreshTokenIds = userRepository.findRefreshTokenIdsForInactiveUsers(threshold);
//
//            // 2. INACTIVE 상태이고 30일 이상 지난 사용자 삭제
//            int deletedUserCount = userRepository.deleteByStatusAndUpdatedAtBefore(threshold);
//
//            // 3. 조회한 RefreshToken ID로 RefreshToken 삭제
//            if (!refreshTokenIds.isEmpty()) {
//                refreshTokenRepository.deleteByIds(refreshTokenIds);
//            }
//
//            log.info("[스케줄러] : inactive 상태이고 updated_at이 " + expirationDays + "일 지난 사용자 삭제");
//            log.info("[스케줄러] : 삭제된 사용자 수 : " + deletedUserCount);
//        } catch (Exception e) {
//            log.error("[스케줄러] : 사용자 삭제 실패", e);
//        }
//    }
}
