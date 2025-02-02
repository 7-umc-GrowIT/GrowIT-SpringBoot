package umc.GrowIT.Server.service.kakaoService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import org.springframework.http.HttpStatusCode;
import reactor.core.publisher.Mono;
import umc.GrowIT.Server.apiPayload.exception.AuthHandler;
import umc.GrowIT.Server.web.dto.OAuthDTO.OAuthResponseDTO;

import static umc.GrowIT.Server.apiPayload.code.status.ErrorStatus.KAKAO_AUTH_CODE_ERROR;

@Service
@RequiredArgsConstructor
public class KakaoServiceImpl implements KakaoService {

    private final WebClient kakaoWebClient;

    private String grantType = "authorization_code";
    @Value("${spring.oauth2.client.kakao.client-id}")
    private String clientId;
    @Value("${spring.oauth2.client.kakao.redirect-uri}")
    private String redirectUri;
    @Value("${spring.oauth2.client.kakao.client-secret}")
    private String clientSecret;

    //인가 코드로 카카오 서버에서 Access, Refresh Token 을 얻어옴
    @Override
    public Mono<OAuthResponseDTO.KakaoTokenResponseDTO> getToken(String code){
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", grantType);
        requestBody.add("client_id", clientId);
        requestBody.add("redirect_uri", redirectUri);
        requestBody.add("code", code);
        requestBody.add("client_secret", clientSecret);


        return kakaoWebClient.post()
                .uri("/oauth/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED) //요청 헤더
                .bodyValue(requestBody) //요청 본문에 추가
                .retrieve() //서버 응답 가져오기
                .onStatus(HttpStatusCode::is4xxClientError, response ->
                        Mono.error(new AuthHandler(KAKAO_AUTH_CODE_ERROR)))
                .bodyToMono(OAuthResponseDTO.KakaoTokenResponseDTO.class); //응답 본문 -> Mono

    }
}
