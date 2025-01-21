package umc.GrowIT.Server.service.authService;

import umc.GrowIT.Server.web.dto.UserDTO.KakaoResponseDTO;


public interface KakaoService {

    KakaoResponseDTO.KakaoTokenDTO getToken(String code);

}
