package umc.GrowIT.Server.service.oAuthService.appleService;

import static umc.GrowIT.Server.apiPayload.code.status.ErrorStatus.APPLE_PARSE_FAILED;
import static umc.GrowIT.Server.apiPayload.code.status.ErrorStatus.INVALID_APPLE_CLIENT_SETTING;
import static umc.GrowIT.Server.apiPayload.code.status.ErrorStatus.INVALID_APPLE_ID_TOKEN;
import static umc.GrowIT.Server.apiPayload.code.status.ErrorStatus.INVALID_AUTHORIZATION_CODE;
import static umc.GrowIT.Server.apiPayload.code.status.ErrorStatus._INTERNAL_SERVER_ERROR;
import static umc.GrowIT.Server.converter.OAuthAccountConverter.toOAuthAccount;
import static umc.GrowIT.Server.converter.OAuthConverter.toOAuthLoginDTO;
import static umc.GrowIT.Server.converter.OAuthConverter.toOAuthUserInfoDTO;
import static umc.GrowIT.Server.converter.UserConverter.toLoginResponseDTO;
import static umc.GrowIT.Server.domain.enums.LoginMethod.SOCIAL;

import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.Date;
import java.util.Optional;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import umc.GrowIT.Server.apiPayload.exception.OAuthHandler;
import umc.GrowIT.Server.domain.OAuthAccount;
import umc.GrowIT.Server.domain.OAuthAccount;
import umc.GrowIT.Server.domain.User;
import umc.GrowIT.Server.domain.enums.LoginMethod;
import umc.GrowIT.Server.repository.OAuthAccountRepository;
import umc.GrowIT.Server.repository.UserRepository;
import umc.GrowIT.Server.service.userService.CustomUserDetailsService;
import umc.GrowIT.Server.service.userService.UserCommandService;
import umc.GrowIT.Server.util.JwtTokenUtil;
import umc.GrowIT.Server.web.dto.OAuthDTO.OAuthApiResponseDTO;
import umc.GrowIT.Server.web.dto.OAuthDTO.OAuthRequestDTO;
import umc.GrowIT.Server.web.dto.OAuthDTO.OAuthResponseDTO;
import umc.GrowIT.Server.web.dto.TokenDTO.TokenResponseDTO;

@RequiredArgsConstructor
@Slf4j
@Service
public class AppleServiceImpl implements AppleService {
    private final OAuthAccountRepository oAuthAccountRepository;
    private final UserRepository userRepository;
    private final UserCommandService userCommandService;
    private final JwtTokenUtil jwtTokenUtil;
    private final CustomUserDetailsService customUserDetailsService;

    private final WebClient appleAuthWebClient;
    @Value("${spring.oauth2.client.ios-apple.client-id}")
    private String clientId;
    @Value("${spring.oauth2.client.ios-apple.client-secret}")
    private String clientSecret;
    @Value("${spring.oauth2.client.ios-apple.redirect-uri}")
    private String redirectUri;
    @Value("${spring.oauth2.client.ios-apple.grant-type}")
    private String grantType;

    @Override
    public String requestToken(OAuthRequestDTO.SocialLoginDTO socialLoginDTO) {
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", grantType);
        requestBody.add("client_id", clientId);
        requestBody.add("redirect_uri", redirectUri);
        requestBody.add("code", socialLoginDTO.getCode());
        requestBody.add("client_secret", clientSecret);

        OAuthApiResponseDTO.AppleTokenResponseDTO response = appleAuthWebClient.post()
                .uri("/auth/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue(requestBody)
                .retrieve()
                .onStatus(HttpStatusCode::isError, errorResponse ->
                        errorResponse.bodyToMono(String.class)
                                .flatMap(errorBody -> {
                                    log.error("Apple token API error: {}", errorBody);
                                    if (errorBody.contains("invalid_client")) {
                                        // Apple Client 설정값이 유효하지 않은 경우
                                        return Mono.error(new OAuthHandler(INVALID_APPLE_CLIENT_SETTING));
                                    } else if (errorBody.contains("invalid_grant")) {
                                        // Authorization code 유효하지 않은 경우
                                        return Mono.error(new OAuthHandler(INVALID_AUTHORIZATION_CODE));
                                    }
                                    return Mono.error(new OAuthHandler(_INTERNAL_SERVER_ERROR));
                                })
                )
                .bodyToMono(OAuthApiResponseDTO.AppleTokenResponseDTO.class)
                .block();
        return response.getId_token();
    }

    @Override
    public OAuthApiResponseDTO.OAuthUserInfoDTO verifyToken(String idToken) {
        try {
            // 토큰 파싱
            SignedJWT idTokenJwt = SignedJWT.parse(idToken);

            // Apple JWKs 가져오기
            String jwksStr = appleAuthWebClient.get()
                    .uri("/auth/oauth2/v2/keys")
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            JWKSet jwkSet = JWKSet.parse(jwksStr);

            // ID Token 의 kid 와 일치하는 JWK 찾고 RSA 공개키로 변환
            JWK jwk = jwkSet.getKeyByKeyId(idTokenJwt.getHeader().getKeyID());
            RSAPublicKey rsaKey = ((RSAKey) jwk).toRSAPublicKey();

            // ID Token Signature 검증
            if (!idTokenJwt.verify(new RSASSAVerifier(rsaKey))) {
                throw new OAuthHandler(INVALID_APPLE_ID_TOKEN);
            }

            // ID Token Claims 검증
            JWTClaimsSet claims = idTokenJwt.getJWTClaimsSet();
            if (!claims.getIssuer().equals("https://appleid.apple.com")) {
                throw new OAuthHandler(INVALID_APPLE_ID_TOKEN);
            } else if (!claims.getAudience().get(0).equals(clientId)) {
                throw new OAuthHandler(INVALID_APPLE_ID_TOKEN);
            } else if (!claims.getExpirationTime().after(new Date())) {
                throw new OAuthHandler(INVALID_APPLE_ID_TOKEN);
            }
            return toOAuthUserInfoDTO(claims.getSubject(), claims.getStringClaim("email"));

        } catch (ParseException parseException) {
            log.error("Failed to parse Apple ID Token or JWKs", parseException);
            throw new OAuthHandler(APPLE_PARSE_FAILED);
        } catch (JOSEException joseException) {
            log.error("Failed to verify Apple ID Token signature", joseException);
            throw new OAuthHandler(INVALID_APPLE_ID_TOKEN);
        } catch (OAuthHandler oAuthHandlerException) {
            throw oAuthHandlerException;
        } catch (Exception e) {
            log.error("Unexpected error while verifying Apple ID Token", e);
            throw new OAuthHandler(_INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public OAuthResponseDTO.OAuthLoginDTO socialLogin(OAuthRequestDTO.SocialLoginDTO socialLoginDTO) {
        // ID Token 발급 및 토큰 검증
        String idToken = requestToken(socialLoginDTO);
        OAuthApiResponseDTO.OAuthUserInfoDTO oAuthUserInfoDTO = verifyToken(idToken);

        String email = oAuthUserInfoDTO.getEmail();
        String socialId = oAuthUserInfoDTO.getSocialId();
        String name = socialLoginDTO.getName();

        Optional<OAuthAccount> oAuthAccount = oAuthAccountRepository.findBySocialId(socialId);
        Optional<User> user = userRepository.findByPrimaryEmail(email);

        User existingUser = null;
        if (user.isPresent()) existingUser = user.get();

        // 애플 이메일로 이메일 회원가입이 되어있지 않고 애플 로그인 기록이 없는 경우
        // 회원 약관 정보가 없기 때문에 회원가입 처리
        if (user.isEmpty()) {
            oAuthUserInfoDTO.setName(name);
            return toOAuthLoginDTO(true, oAuthUserInfoDTO, toLoginResponseDTO(null, null));
        // 애플 이메일로 이메일 회원가입이 되어있고 애플 로그인 기록이 없는 경우
        // OAuthAccount 저장 및 기존 계정으로 로그인 처리
        } else if (oAuthAccount.isEmpty()) {
            userCommandService.checkUserInactive(existingUser); // 탈퇴한 회원인지 확인

            OAuthAccount newOAuthAccount = toOAuthAccount(oAuthUserInfoDTO, existingUser);
            oAuthAccountRepository.save(newOAuthAccount);
        }
        // 애플 로그인 기록 있는 경우 또는 OAuthAccount 저장 후 AT, RT 토큰 발급 및 RT DB 저장
        TokenResponseDTO.TokenDTO tokenDTO = jwtTokenUtil.generateToken(
                customUserDetailsService.loadUserByUsername(existingUser.getPrimaryEmail())
        );
        userCommandService.setRefreshToken(tokenDTO.getRefreshToken(), existingUser);

        return toOAuthLoginDTO(false, null, toLoginResponseDTO(tokenDTO, SOCIAL)); // 로그인 처리
        }
}
