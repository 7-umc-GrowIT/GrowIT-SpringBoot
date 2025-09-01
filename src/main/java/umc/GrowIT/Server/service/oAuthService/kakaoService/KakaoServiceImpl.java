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
import umc.GrowIT.Server.apiPayload.exception.OAuthHandler;
import umc.GrowIT.Server.apiPayload.exception.UserHandler;
import umc.GrowIT.Server.domain.OAuthAccount;
import umc.GrowIT.Server.domain.User;
import umc.GrowIT.Server.repository.OAuthAccountRepository;
import umc.GrowIT.Server.repository.UserRepository;
import umc.GrowIT.Server.service.userService.CustomUserDetailsService;
import umc.GrowIT.Server.service.userService.UserCommandService;
import umc.GrowIT.Server.util.JwtTokenUtil;
import umc.GrowIT.Server.web.dto.OAuthDTO.OAuthApiResponseDTO;
import umc.GrowIT.Server.web.dto.OAuthDTO.OAuthRequestDTO;
import umc.GrowIT.Server.web.dto.OAuthDTO.OAuthResponseDTO;
import umc.GrowIT.Server.web.dto.TokenDTO.TokenResponseDTO;

import static umc.GrowIT.Server.apiPayload.code.status.ErrorStatus.*;
import static umc.GrowIT.Server.converter.OAuthAccountConverter.toOAuthAccount;
import static umc.GrowIT.Server.converter.OAuthConverter.toOAuthLoginDTO;
import static umc.GrowIT.Server.converter.OAuthConverter.toOAuthUserInfoDTO;
import static umc.GrowIT.Server.domain.enums.LoginMethod.SOCIAL;

@Service
@RequiredArgsConstructor
@Slf4j
public class KakaoServiceImpl implements KakaoService {

    private final UserRepository userRepository;
    private final OAuthAccountRepository oAuthAccountRepository;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserCommandService userCommandService;

    private final WebClient kakaoAuthWebClient;
    private final WebClient kakaoApiWebClient;

    private String grantType = "authorization_code";
    @Value("${spring.oauth2.client.ios-kakao.client-id}")
    private String clientId;
    @Value("${spring.oauth2.client.ios-kakao.redirect-uri}")
    private String redirectUri;
    @Value("${spring.oauth2.client.ios-kakao.client-secret}")
    private String clientSecret;

    /**
     * 사용자 정보를 카카오 서버에 요청하고 저장합니다.
     *
     * @param code 카카오 서버로부터 받은 인가 코드
     */
    public OAuthApiResponseDTO.KakaoUserInfoResponseDTO saveKakaoUserInfo(OAuthRequestDTO.SocialLoginDTO socialLoginDTO) {
        OAuthApiResponseDTO.KakaoTokenResponseDTO kakaoTokenResponse = requestKakaoToken(socialLoginDTO);

        if (kakaoTokenResponse == null)
            throw new OAuthHandler(INVALID_AUTHORIZATION_CODE);

        String accessToken = kakaoTokenResponse.getAccess_token();
        return requestKakaoUserInfo(accessToken);
    }

    /**
     * 인가 코드로 카카오 서버에서 Access Token, Refresh Token 을 얻어옵니다
     *
     * @param code 카카오 서버로부터 받은 인가 코드
     * @return AccessToken, RefreshToken 등
     * @throws AuthHandler 인가 코드 잘못 주었을 때 예외 처리
     */
    @Override
    public OAuthApiResponseDTO.KakaoTokenResponseDTO requestKakaoToken(OAuthRequestDTO.SocialLoginDTO socialLoginDTO){
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", grantType);
        requestBody.add("client_id", clientId);
        requestBody.add("redirect_uri", redirectUri);
        requestBody.add("code", socialLoginDTO.getCode());
        requestBody.add("client_secret", clientSecret);

        return kakaoAuthWebClient.post()
                .uri("/oauth/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED) //요청 헤더
                .bodyValue(requestBody) //요청 본문에 추가
                .retrieve() //서버 응답 가져오기
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> Mono.error(new OAuthHandler(INVALID_AUTHORIZATION_CODE)))
                .bodyToMono(OAuthApiResponseDTO.KakaoTokenResponseDTO.class) //응답 본문 -> Mono
                .block();
    }

    /**
     * AccessToken 으로 카카오 서버에서 사용자 정보를 얻어옵니다
     *
     * @param accessToken 카카오 서버로부터 받은 AccessToken
     * @return 사용자 정보 (이메일, 닉네임)
     */
    public OAuthApiResponseDTO.KakaoUserInfoResponseDTO requestKakaoUserInfo(String accessToken) {
            return kakaoApiWebClient.get()
                    .uri("/v2/user/me")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    .retrieve()
                    .bodyToMono(OAuthApiResponseDTO.KakaoUserInfoResponseDTO.class)
                    .block();
    }

    /**
     * 기존 계정이 있는 경우 (카카오 로그인을 한 적이 있는 경우) 로그인 처리,
     * 기존 계정이 없는 경우 회원가입을 위한 약관 정보 요청을 유도
     *
     * @param code 카카오 서버로부터 받은 인증 코드
     * @return 회원가입 필요 여부, AT/RT
     */
    @Transactional
    public OAuthResponseDTO.OAuthLoginDTO loginKakao(OAuthRequestDTO.SocialLoginDTO socialLoginDTO) {
        // 사용자 정보 얻어옴
        OAuthApiResponseDTO.KakaoUserInfoResponseDTO kakaoUserInfoResponse = saveKakaoUserInfo(socialLoginDTO);

        OAuthApiResponseDTO.OAuthUserInfoDTO oAuthUserInfoDTO = toOAuthUserInfoDTO(kakaoUserInfoResponse);

        // DB에 카카오 이메일과 일치하는 이메일 있는지 확인 (일부 연동)
        // TODO: 카카오에서 얻어온 사용자 본인 인증 정보와 DB 의 본인 인증 정보 일치 확인 추가 (연동)
        if (!userRepository.existsByPrimaryEmail(oAuthUserInfoDTO.getEmail()))
            return toOAuthLoginDTO(true, oAuthUserInfoDTO, null); // 최초 회원가입 요청
        else {
            User user = userRepository.findByPrimaryEmail(oAuthUserInfoDTO.getEmail())
                    .orElseThrow(() -> new UserHandler(_BAD_REQUEST));
            userCommandService.checkUserInactive(user); // 탈퇴한 회원인지 확인
            // 이메일 회원가입은 했지만 카카오 최초 로그인인 경우 OAuthAccount 엔티티 저장
            if (!oAuthAccountRepository.existsBySocialId(String.valueOf(oAuthUserInfoDTO.getSocialId()))) {
                OAuthAccount oAuthAccount = toOAuthAccount(oAuthUserInfoDTO, user);
                oAuthAccountRepository.save(oAuthAccount);
            }
            // AT, RT 토큰 발급 및 RT DB 저장
            TokenResponseDTO.TokenDTO tokenDTO = jwtTokenUtil.generateToken(
                    customUserDetailsService.loadUserByUsername(user.getPrimaryEmail()));
            userCommandService.setRefreshToken(tokenDTO.getRefreshToken(), user);
            tokenDTO.setLoginMethod(SOCIAL);

            return toOAuthLoginDTO(false, null, tokenDTO); // 로그인 처리
        }
    }
}
