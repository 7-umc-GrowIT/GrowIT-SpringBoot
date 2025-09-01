package umc.GrowIT.Server.service.oAuthService.appleService;

import umc.GrowIT.Server.web.dto.OAuthDTO.OAuthApiResponseDTO;
import umc.GrowIT.Server.web.dto.OAuthDTO.OAuthRequestDTO;
import umc.GrowIT.Server.web.dto.OAuthDTO.OAuthResponseDTO;

public interface AppleService {
    String requestToken(OAuthRequestDTO.SocialLoginDTO socialLoginDTO); // 애플 인증 서버에 토큰 요청
    OAuthApiResponseDTO.OAuthUserInfoDTO verifyToken(String idToken); // 토큰 검증
    OAuthResponseDTO.OAuthLoginDTO socialLogin (OAuthRequestDTO.SocialLoginDTO socialLoginDTO);
}
