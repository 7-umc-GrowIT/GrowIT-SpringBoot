package umc.GrowIT.Server.service.oAuthService.kakaoService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import org.springframework.http.HttpStatusCode;
import reactor.core.publisher.Mono;
import umc.GrowIT.Server.apiPayload.exception.AuthHandler;
import umc.GrowIT.Server.domain.User;
import umc.GrowIT.Server.repository.OAuthAccountRepository;
import umc.GrowIT.Server.service.userService.UserCommandService;
import umc.GrowIT.Server.util.JwtTokenUtil;
import umc.GrowIT.Server.web.dto.OAuthDTO.OAuthResponseDTO;
import umc.GrowIT.Server.web.dto.UserDTO.UserResponseDTO;

import static umc.GrowIT.Server.apiPayload.code.status.ErrorStatus.KAKAO_AUTH_CODE_ERROR;
import static umc.GrowIT.Server.converter.UserConverter.toKakaoLoginDTO;

@Service
@RequiredArgsConstructor
@Slf4j
public class KakaoServiceImpl implements KakaoService {

    private final OAuthAccountRepository oAuthAccountRepository;
    private final UserCommandService userCommandService;
    private final JwtTokenUtil jwtTokenUtil;

    private final WebClient kakaoAuthWebClient;
    private final WebClient kakaoApiWebClient;

    private String grantType = "authorization_code";
    @Value("${spring.oauth2.client.kakao.client-id}")
    private String clientId;
    @Value("${spring.oauth2.client.kakao.redirect-uri}")
    private String redirectUri;
    @Value("${spring.oauth2.client.kakao.client-secret}")
    private String clientSecret;

    /**
     * 인가 코드로 카카오 서버에서 Access Token, Refresh Token 을 얻어옵니다
     *
     * @param code 카카오 서버로부터 받은 인가 코드
     * @return AccessToken, RefreshToken 등
     * @throws AuthHandler 인가 코드 잘못 주었을 때 예외 처리
     */
    @Override
    public Mono<OAuthResponseDTO.KakaoTokenResponseDTO> requestKakaoToken(String code){
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", grantType);
        requestBody.add("client_id", clientId);
        requestBody.add("redirect_uri", redirectUri);
        requestBody.add("code", code);
        requestBody.add("client_secret", clientSecret);

        return kakaoAuthWebClient.post()
                .uri("/oauth/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED) //요청 헤더
                .bodyValue(requestBody) //요청 본문에 추가
                .retrieve() //서버 응답 가져오기
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> Mono.error(new AuthHandler(KAKAO_AUTH_CODE_ERROR)))
                .bodyToMono(OAuthResponseDTO.KakaoTokenResponseDTO.class); //응답 본문 -> Mono
    }

    /**
     * AccessToken 으로 카카오 서버에서 사용자 정보를 얻어옵니다
     *
     * @param accessToken 카카오 서버로부터 받은 AccessToken
     * @return 사용자 정보 (이메일, 닉네임)
     */
    public Mono<OAuthResponseDTO.KakaoUserInfoResponseDTO> requestKakaoUserInfo(String accessToken) {
            return kakaoApiWebClient.get()
                    .uri("/v2/user/me")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    .retrieve()
                    .bodyToMono(OAuthResponseDTO.KakaoUserInfoResponseDTO.class);
    }

    public Mono<OAuthResponseDTO.KakaoUserInfoResponseDTO> getKakaoUserInfo(String code) {
        return requestKakaoToken(code)
                .flatMap(tokenResponseBody -> { //비동기 작업 체이닝
                    String accessToken = tokenResponseBody.getAccess_token();
                    log.info("AT : " + accessToken);
                    return requestKakaoUserInfo(accessToken);
                });
    }

    @Transactional
    public Mono<UserResponseDTO.KakaoLoginDTO> loginKakao(String code) {
        return getKakaoUserInfo(code)
                .flatMap(kakaoUserInfo ->
                        Mono.justOrEmpty(oAuthAccountRepository.findByProviderId(kakaoUserInfo.getId()))
                                .flatMap(oAuthAccount -> {
                                    // 기존 계정이 있는 경우 로그인 처리 AT, RT 발급
                                    User user = oAuthAccount.getUser();
                                    UserResponseDTO.TokenDTO tokenDTO = jwtTokenUtil.generateToken(
                                            userCommandService.createUserDetails(user));
                                    userCommandService.setRefreshToken(tokenDTO.getRefreshToken(), user);
                                    return Mono.just(toKakaoLoginDTO(false, tokenDTO));
                                })
                                // 기존 계정이 없을 경우 회원 가입에 필요한 약관 동의 목록 요청
                                .switchIfEmpty(Mono.just(toKakaoLoginDTO(true, null)))
                );
    }

}
