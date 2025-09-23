package umc.GrowIT.Server.service.oAuthService;

import umc.GrowIT.Server.web.dto.AuthDTO.AuthResponseDTO;
import umc.GrowIT.Server.web.dto.OAuthDTO.OAuthRequestDTO;
import umc.GrowIT.Server.web.dto.TokenDTO.TokenResponseDTO;

public interface OAuthCommandService {
    AuthResponseDTO.LoginResponseDTO signupSocial(OAuthRequestDTO.OAuthUserInfoAndUserTermsDTO oAuthUserInfoAndUserTermsDTO);
}
