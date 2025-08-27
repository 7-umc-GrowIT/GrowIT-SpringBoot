package umc.GrowIT.Server.service.oAuthService.kakaoService;

import umc.GrowIT.Server.web.dto.OAuthDTO.OAuthApiResponseDTO;
import umc.GrowIT.Server.web.dto.OAuthDTO.OAuthRequestDTO;
import umc.GrowIT.Server.web.dto.OAuthDTO.OAuthResponseDTO;


public interface KakaoService {

    OAuthApiResponseDTO.KakaoTokenResponseDTO requestKakaoToken(OAuthRequestDTO.SocialLoginDTO socialLoginDTO);
    OAuthApiResponseDTO.KakaoUserInfoResponseDTO requestKakaoUserInfo(String accessToken);
    OAuthApiResponseDTO.KakaoUserInfoResponseDTO saveKakaoUserInfo(OAuthRequestDTO.SocialLoginDTO socialLoginDTO);
    OAuthResponseDTO.OAuthLoginDTO loginKakao(OAuthRequestDTO.SocialLoginDTO socialLoginDTO);
}
