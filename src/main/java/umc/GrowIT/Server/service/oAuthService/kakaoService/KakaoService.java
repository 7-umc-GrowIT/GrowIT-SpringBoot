package umc.GrowIT.Server.service.oAuthService.kakaoService;

import reactor.core.publisher.Mono;
import umc.GrowIT.Server.web.dto.OAuthDTO.OAuthResponseDTO;
import umc.GrowIT.Server.web.dto.UserDTO.UserResponseDTO;


public interface KakaoService {

    OAuthResponseDTO.KakaoTokenResponseDTO requestKakaoToken(String code);
    OAuthResponseDTO.KakaoUserInfoResponseDTO requestKakaoUserInfo(String accessToken);
    OAuthResponseDTO.KakaoUserInfoResponseDTO saveKakaoUserInfo(String code);
    UserResponseDTO.KakaoLoginDTO loginKakao(String code);
}
