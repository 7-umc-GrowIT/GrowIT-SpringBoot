package umc.GrowIT.Server.service.refreshTokenService;

import umc.GrowIT.Server.domain.RefreshToken;
import umc.GrowIT.Server.domain.User;

public interface RefreshTokenCommandService {

    RefreshToken createRefreshToken(String refreshToken, User user);


}
