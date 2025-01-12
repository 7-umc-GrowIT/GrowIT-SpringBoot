package umc.GrowIT.Server.service;

import umc.GrowIT.Server.dto.KakaoResponseDTO;


public interface KakaoService {

    KakaoResponseDTO.KakaoTokenDTO getToken(String code);

}
