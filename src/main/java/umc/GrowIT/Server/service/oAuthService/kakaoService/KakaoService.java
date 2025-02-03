package umc.GrowIT.Server.service.oAuthService.kakaoService;

import reactor.core.publisher.Mono;
import umc.GrowIT.Server.web.dto.OAuthDTO.OAuthResponseDTO;
import umc.GrowIT.Server.web.dto.UserDTO.UserResponseDTO;


public interface KakaoService {

    Mono<OAuthResponseDTO.KakaoTokenResponseDTO> requestKakaoToken(String code);
    Mono<OAuthResponseDTO.KakaoUserInfoResponseDTO> requestKakaoUserInfo(String accessToken);
    Mono<OAuthResponseDTO.KakaoUserInfoResponseDTO> getKakaoUserInfo(String code);
    Mono<UserResponseDTO.KakaoLoginDTO> loginKakao(String code);
}
