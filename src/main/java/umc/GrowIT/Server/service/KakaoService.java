package umc.GrowIT.Server.service;

import umc.GrowIT.Server.web.dto.UserDTO.KakaoResponseDTO;


public interface KakaoService {

    KakaoResponseDTO.KakaoTokenDTO getToken(String code);

}
