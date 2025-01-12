package umc.GrowIT.Server.service.userService;

import umc.GrowIT.Server.web.dto.UserDTO.KakaoResponseDTO;


public interface KakaoService {

    KakaoResponseDTO.KakaoTokenDTO getToken(String code);

}
