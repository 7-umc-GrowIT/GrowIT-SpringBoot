package umc.GrowIT.Server.service.oAuthService.kakaoService;

import umc.GrowIT.Server.web.dto.OAuthDTO.OAuthApiResponseDTO;
import umc.GrowIT.Server.web.dto.OAuthDTO.OAuthRequestDTO;
import umc.GrowIT.Server.web.dto.OAuthDTO.OAuthResponseDTO;
import umc.GrowIT.Server.web.dto.UserDTO.UserResponseDTO;


public interface KakaoService {

    OAuthApiResponseDTO.KakaoTokenResponseDTO requestKakaoToken(OAuthRequestDTO.SocialLoginDTO socialLoginDTO);
    OAuthApiResponseDTO.KakaoUserInfoResponseDTO requestKakaoUserInfo(String accessToken);
    OAuthApiResponseDTO.KakaoUserInfoResponseDTO saveKakaoUserInfo(OAuthRequestDTO.SocialLoginDTO socialLoginDTO);
    OAuthResponseDTO.KakaoLoginDTO loginKakao(OAuthRequestDTO.SocialLoginDTO socialLoginDTO);
}
