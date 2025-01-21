package umc.GrowIT.Server.service.refreshTokenService;

import umc.GrowIT.Server.domain.RefreshToken;
import umc.GrowIT.Server.domain.User;
import umc.GrowIT.Server.web.dto.UserDTO.UserRequestDTO;
import umc.GrowIT.Server.web.dto.UserDTO.UserResponseDTO;

public interface RefreshTokenCommandService {

    RefreshToken createRefreshToken(String refreshToken, User user);

    UserResponseDTO.AccessTokenDTO reissueToken(UserRequestDTO.ReissueDTO reissueDTO);


}
