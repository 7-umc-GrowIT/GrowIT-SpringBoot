package umc.GrowIT.Server.service.authService;

import jakarta.validation.Valid;
import umc.GrowIT.Server.domain.enums.AuthType;
import umc.GrowIT.Server.web.dto.AuthDTO.AuthRequestDTO;
import umc.GrowIT.Server.web.dto.AuthDTO.AuthResponseDTO;
import umc.GrowIT.Server.web.dto.LogoutDTO.LogoutResponseDTO;

public interface AuthService {
    AuthResponseDTO.SendAuthEmailResponseDTO sendAuthEmail(AuthType type, AuthRequestDTO.SendAuthEmailRequestDTO request);

    AuthResponseDTO.VerifyAuthCodeResponseDTO verifyAuthCode(AuthRequestDTO.VerifyAuthCodeRequestDTO request);

    LogoutResponseDTO logout(Long userId);
}
