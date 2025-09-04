    package umc.GrowIT.Server.util;

    import lombok.RequiredArgsConstructor;
    import org.springframework.stereotype.Service;
    import umc.GrowIT.Server.domain.*;
    import umc.GrowIT.Server.domain.enums.CreditSource;
    import umc.GrowIT.Server.repository.CreditHistoryRepository;
    import umc.GrowIT.Server.repository.UserChallengeRepository;
    import umc.GrowIT.Server.util.dto.CreditGrantResult;

    import java.time.LocalDate;
    import java.time.format.DateTimeFormatter;

    @Service
    @RequiredArgsConstructor
    public class CreditUtil {

        private final CreditHistoryRepository creditHistoryRepository;
        private final UserChallengeRepository userChallengeRepository;

        // 앱 자체에서 처음 일기 작성 시 적립되는 크레딧
        private static final int FIRST_DIARY_CREDIT = 5;

        // 날짜별로 처음 일기 작성 시 적립되는 크레딧
        private static final int DAILY_FIRST_DIARY_CREDIT = 2;

        // 챌린지 인증 작성 시 적립되는 크레딧
        private static final int CHALLENGE_CREDIT = 1;

        // 일기 첫작성으로 인한 크레딧 제공 기록 및 사용자 크레딧 최신화
        public CreditGrantResult grantDiaryCredit(User user, Diary diary) {
            int amount = 0;

            // 1. 사용자의 상황 판단 통해 크레딧 결정
            // 앱 자체 일기 첫작성 시
            if (isFirstDiaryEver(user)) {
                amount = FIRST_DIARY_CREDIT;
            }
            // 날짜별 일기 첫작성 시
            else if (!hasDiaryCreditForDate(user, diary.getDate())) {
                amount = DAILY_FIRST_DIARY_CREDIT;
            }
            // 날짜별 일기 재작성 시
            else {
                return new CreditGrantResult(false, 0);
            }

            // 2. 크레딧 기록
            CreditHistory credit = CreditHistory.builder()
                    .user(user)
                    .source(CreditSource.DIARY)
                    .referenceId(diary.getId())
                    .date(diary.getDate())
                    .amount(amount)
                    .description(String.format("[%s] 일기 작성", diary.getDate().format(DateTimeFormatter.ofPattern("MM-dd"))))
                    .build()
                    ;

            creditHistoryRepository.save(credit);

            // 3. 사용자 크레딧 최신화
            user.updateCurrentCredit(user.getCurrentCredit() + amount);
            user.updateTotalCredit(user.getTotalCredit() + amount);

            // 4. 크레딧 제공 O
            return new CreditGrantResult(true, amount);
        }

        // 아이템 구매로 인한 사용자 크레딧 최신화
        public void deductItemCredit(User user, UserItem userItem, Item item) {
            // 1. 크레딧 사용 기록 (음수로 저장)
            CreditHistory credit = CreditHistory.builder()
                    .user(user)
                    .source(CreditSource.ITEM)
                    .referenceId(userItem.getId())
                    .amount(-item.getPrice())
                    .description(String.format("[%s] 아이템 구매", item.getName()))
                    .build()
                    ;

            creditHistoryRepository.save(credit);

            // 2. 사용자 크레딧 최신화
            user.updateCurrentCredit(user.getCurrentCredit() - item.getPrice());
        }

        // 챌린지 인증으로 인한 크레딧 제공 기록 및 사용자 크레딧 최신화
        public CreditGrantResult grantUserChallengeCredit(User user, UserChallenge userChallenge) {
            // 1. 동일한 날짜의 일기에 대해 인증 완료한 챌린지 개수 가져오기
            long completedCountOnDate = userChallengeRepository.countCompletedOnDateByUserId(user.getId(), userChallenge.getDate());

            if (completedCountOnDate >= 4) {
                return new CreditGrantResult(false, 0); // 챌린지 인증 4번째부터는 크레딧 제공 x
            }

            // 2. 크레딧 기록
            CreditHistory credit = CreditHistory.builder()
                    .user(user)
                    .source(CreditSource.CHALLENGE)
                    .referenceId(userChallenge.getId())
                    .date(userChallenge.getDate())
                    .amount(CHALLENGE_CREDIT)
                    .description(String.format("[%s] 챌린지 완료",
                            userChallenge.getDate().format(DateTimeFormatter.ofPattern("MM-dd"))))
                    .build()
                    ;

            creditHistoryRepository.save(credit);

            // 3. 사용자 크레딧 최신화
            user.updateCurrentCredit(user.getCurrentCredit() + CHALLENGE_CREDIT);
            user.updateTotalCredit(user.getTotalCredit() + CHALLENGE_CREDIT);

            // 4. 크레딧 제공 O
            return new CreditGrantResult(true, CHALLENGE_CREDIT);
        }


        /*
            이 아래부터 헬퍼메소드
        */
        // 앱 최초 일기 작성 여부 확인 (가입 후 최초 1회)
        private boolean isFirstDiaryEver(User user) {
            return !creditHistoryRepository.existsByUserAndSource(user, CreditSource.DIARY);
        }
        // 해당 날짜에 일기 첫작성으로 인해서 크레딧을 이미 받았는지 확인
        public boolean hasDiaryCreditForDate(User user, LocalDate date) {
            return creditHistoryRepository.existsByUserAndDateAndSource(user, date, CreditSource.DIARY);
        }
    }


