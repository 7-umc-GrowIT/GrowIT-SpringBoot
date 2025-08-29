package umc.GrowIT.Server.service.oAuthService.kakaoService;

import umc.GrowIT.Server.web.dto.OAuthDTO.OAuthApiResponseDTO;
import umc.GrowIT.Server.web.dto.OAuthDTO.OAuthResponseDTO;


public interface KakaoService {

    OAuthApiResponseDTO.KakaoTokenResponseDTO requestKakaoToken(String code);
    OAuthApiResponseDTO.KakaoUserInfoResponseDTO requestKakaoUserInfo(String accessToken);
    OAuthApiResponseDTO.KakaoUserInfoResponseDTO saveKakaoUserInfo(String code);
    OAuthResponseDTO.KakaoLoginDTO loginKakao(String code);
}
