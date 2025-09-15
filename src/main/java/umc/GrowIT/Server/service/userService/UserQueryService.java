package umc.GrowIT.Server.service.userService;

import umc.GrowIT.Server.domain.enums.CreditTransactionType;
import umc.GrowIT.Server.web.dto.UserDTO.UserResponseDTO;

public interface UserQueryService {
    boolean isUserInactive(Long userId);

    UserResponseDTO.MyPageDTO getMyPage(Long userId);

    UserResponseDTO.CreditHistoryResponseDTO getCreditHistory(Long userId, int year, int month, CreditTransactionType type, int page);

    UserResponseDTO.EmailResponseDTO getMyEmail(Long userId);

    boolean hasVoiceDiaries(Long userId);
}
