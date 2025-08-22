package umc.GrowIT.Server.service.oAuthService.appleService;

import static umc.GrowIT.Server.apiPayload.code.status.ErrorStatus.APPLE_PARSE_FAILED;
import static umc.GrowIT.Server.apiPayload.code.status.ErrorStatus.INVALID_APPLE_CLIENT_SETTING;
import static umc.GrowIT.Server.apiPayload.code.status.ErrorStatus.INVALID_APPLE_ID_TOKEN;
import static umc.GrowIT.Server.apiPayload.code.status.ErrorStatus.INVALID_AUTHORIZATION_CODE;
import static umc.GrowIT.Server.apiPayload.code.status.ErrorStatus._INTERNAL_SERVER_ERROR;

import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.Date;

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
import umc.GrowIT.Server.web.dto.OAuthDTO.OAuthApiResponseDTO;
import umc.GrowIT.Server.web.dto.OAuthDTO.OAuthRequestDTO;

@RequiredArgsConstructor
@Slf4j
@Service
public class AppleServiceImpl implements AppleService {

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
    public void verifyToken(String idToken) {
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

    public OAuthApiResponseDTO.AppleTokenResponseDTO socialLogin (OAuthRequestDTO.SocialLoginDTO socialLoginDTO) {
        // ID Token 발급 및 토큰 검증
        String idToken = requestToken(socialLoginDTO);
        verifyToken(idToken);

        socialLoginDTO.getNickname();
        
        return null;
    }
}
