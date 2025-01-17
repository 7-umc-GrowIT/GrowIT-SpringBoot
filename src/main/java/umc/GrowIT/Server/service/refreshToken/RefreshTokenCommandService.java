package umc.GrowIT.Server.service.refreshToken;

import umc.GrowIT.Server.domain.RefreshToken;
import umc.GrowIT.Server.domain.User;

public interface RefreshTokenCommandService {
    RefreshToken createRefreshToken(String refreshToken, User user);

}
