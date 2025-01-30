package umc.GrowIT.Server.service.kakaoService;

import umc.GrowIT.Server.web.dto.UserDTO.KakaoResponseDTO;


public interface KakaoService {

    KakaoResponseDTO.KakaoTokenDTO getToken(String code);

}
