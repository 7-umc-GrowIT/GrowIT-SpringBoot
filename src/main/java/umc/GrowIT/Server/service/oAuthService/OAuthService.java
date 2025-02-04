package umc.GrowIT.Server.service.oAuthService;

import umc.GrowIT.Server.web.dto.UserDTO.UserRequestDTO;
import umc.GrowIT.Server.web.dto.UserDTO.UserResponseDTO;

public interface OAuthService {
    UserResponseDTO.TokenDTO signupSocial(UserRequestDTO.UserTermsDTO userTermsDTO);
}
