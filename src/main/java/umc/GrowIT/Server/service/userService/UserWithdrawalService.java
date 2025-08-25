package umc.GrowIT.Server.service.userService;

import umc.GrowIT.Server.domain.User;

public interface UserWithdrawalService {
    void deleteUserRelatedData(Long userId, Long refreshTokenId);
}
