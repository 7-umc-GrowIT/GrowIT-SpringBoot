package umc.GrowIT.Server.service.kakaoService;

import reactor.core.publisher.Mono;
import umc.GrowIT.Server.web.dto.OAuthDTO.OAuthResponseDTO;


public interface KakaoService {

    Mono<OAuthResponseDTO.KakaoTokenResponseDTO> getToken(String code);

}
