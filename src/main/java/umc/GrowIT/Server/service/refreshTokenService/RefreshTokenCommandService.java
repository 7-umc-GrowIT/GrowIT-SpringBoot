package umc.GrowIT.Server.service.refreshTokenService;

import umc.GrowIT.Server.domain.RefreshToken;
import umc.GrowIT.Server.domain.User;
import umc.GrowIT.Server.web.dto.TokenDTO.TokenRequestDTO;
import umc.GrowIT.Server.web.dto.TokenDTO.TokenResponseDTO;

public interface RefreshTokenCommandService {

    RefreshToken createRefreshToken(String refreshToken, User user);

    TokenResponseDTO.AccessTokenDTO reissueToken(TokenRequestDTO.ReissueDTO reissueDTO);

    void deleteRefreshToken(RefreshToken refreshToken);
}
