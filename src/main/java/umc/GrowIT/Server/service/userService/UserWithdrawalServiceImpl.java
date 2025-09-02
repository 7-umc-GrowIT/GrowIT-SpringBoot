package umc.GrowIT.Server.service.userService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import umc.GrowIT.Server.repository.*;

@Service
@RequiredArgsConstructor
public class UserWithdrawalServiceImpl implements UserWithdrawalService {

    private final DiaryKeywordRepository diaryKeywordRepository;
    private final UserChallengeRepository userChallengeRepository;
    private final UserTermRepository userTermRepository;
    private final UserItemRepository userItemRepository;
    private final DiaryRepository diaryRepository;
    private final GroRepository groRepository;
    private final OAuthAccountRepository oAuthAccountRepository;
    private final CreditHistoryRepository creditHistoryRepository;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void deleteUserRelatedData(Long userId, Long refreshTokenId) {
        // 1. 매핑 테이블들 삭제 (user랑 직접 연결 X)
        diaryKeywordRepository.deleteByUserId(userId);

        // 2. 매핑 테이블들 삭제 (user랑 직접 연결 O)
        userChallengeRepository.deleteByUserId(userId);
        userTermRepository.deleteByUserId(userId);
        userItemRepository.deleteByUserId(userId);

        // 3. 연관 테이블들 삭제
        diaryRepository.deleteByUserId(userId);
        groRepository.deleteByUserId(userId);
        oAuthAccountRepository.deleteByUserId(userId);
        creditHistoryRepository.deleteByUserId(userId);

        // 4. User 삭제
        userRepository.deleteById(userId);

        // 5. RefreshToken 삭제 (FK 참조가 없어진 후)
        refreshTokenRepository.deleteById(refreshTokenId);
    }
}
