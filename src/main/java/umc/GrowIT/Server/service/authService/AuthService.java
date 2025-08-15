package umc.GrowIT.Server.service.authService;

import umc.GrowIT.Server.domain.enums.AuthType;
import umc.GrowIT.Server.web.dto.AuthDTO.AuthRequestDTO;
import umc.GrowIT.Server.web.dto.AuthDTO.AuthResponseDTO;

public interface AuthService {
    AuthResponseDTO.SendAuthEmailResponseDTO sendAuthEmail(AuthType type, AuthRequestDTO.SendAuthEmailRequestDTO request);

    AuthResponseDTO.VerifyAuthCodeResponseDTO verifyAuthCode(AuthRequestDTO.VerifyAuthCodeRequestDTO request);

    AuthResponseDTO.LogoutResponseDTO logout(Long userId);
}
