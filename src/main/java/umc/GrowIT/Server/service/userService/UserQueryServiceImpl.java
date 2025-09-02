package umc.GrowIT.Server.service.userService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.GrowIT.Server.apiPayload.code.status.ErrorStatus;
import umc.GrowIT.Server.apiPayload.exception.UserHandler;
import umc.GrowIT.Server.converter.UserConverter;
import umc.GrowIT.Server.domain.CreditHistory;
import umc.GrowIT.Server.domain.Gro;
import umc.GrowIT.Server.domain.User;
import umc.GrowIT.Server.repository.CreditHistoryRepository;
import umc.GrowIT.Server.repository.GroRepository;
import umc.GrowIT.Server.repository.UserRepository;
import umc.GrowIT.Server.domain.enums.CreditTransactionType;
import umc.GrowIT.Server.web.dto.UserDTO.UserResponseDTO;

import java.time.LocalDateTime;
import java.time.YearMonth;

import static umc.GrowIT.Server.domain.enums.UserStatus.INACTIVE;

@Service
@RequiredArgsConstructor
public class UserQueryServiceImpl implements UserQueryService {

    private final UserRepository userRepository;
    private final GroRepository groRepository;
    private final CreditHistoryRepository creditHistoryRepository;

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO.MyPageDTO getMyPage(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));


        Gro gro = groRepository.findByUserId(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.GRO_NOT_FOUND));


        return UserConverter.toMyPageDTO(user, gro);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO.CreditHistoryResponseDTO getCreditHistory(Long userId, int year, int month, CreditTransactionType type, int page) {
        // 1. 사용자 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        // 2. 월 & type에 맞게 조회
        YearMonth ym = YearMonth.of(year, month);
        Pageable pageable = PageRequest.of(page, 10);

        Slice<CreditHistory> histories = getCreditHistoryByMonth(user, ym, type, pageable);

        // 3. 결과 반환
        return UserConverter.toCreditHistoryResponseDTO(histories.getContent(), ym, histories.hasNext());
    }

    /*
        이 아래부터 헬퍼메소드
    */
    public boolean isUserInactive(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        return user.getStatus() == INACTIVE;
    }

    private Slice<CreditHistory> getCreditHistoryByMonth(User user, YearMonth yearMonth,
                                                         CreditTransactionType type, Pageable pageable) {
        LocalDateTime startDateTime = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime endDateTime = yearMonth.atEndOfMonth().atTime(23, 59, 59);

        return switch (type) {
            case ALL -> creditHistoryRepository.findByUserAndCreatedAtBetweenOrderByCreatedAtDesc(
                    user, startDateTime, endDateTime, pageable);
            case EARN -> creditHistoryRepository.findByUserAndCreatedAtBetweenAndAmountGreaterThanOrderByCreatedAtDesc(
                    user, startDateTime, endDateTime, 0, pageable);
            case SPEND -> creditHistoryRepository.findByUserAndCreatedAtBetweenAndAmountLessThanOrderByCreatedAtDesc(
                    user, startDateTime, endDateTime, 0, pageable);
        };
    }
}
