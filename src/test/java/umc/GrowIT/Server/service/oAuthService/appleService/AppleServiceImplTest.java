package umc.GrowIT.Server.service.oAuthService.appleService;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class AppleServiceImplTest {
    private static final String TEST_ID_TOKEN = "eyJraWQiOiJVYUlJRlkyZlc0IiwiYWxnIjoiUlMyNTYifQ.eyJpc3MiOiJodHRwczovL2FwcGxlaWQuYXBwbGUuY29tIiwiYXVkIjoiY29tLkdyb3dJVC5hcHAubG9naW4iLCJleHAiOjE3NTYyNDYyMDMsImlhdCI6MTc1NjE1OTgwMywic3ViIjoiMDAxOTM4LjRmZjE0NDMzZTNhOTRiNzNiYzg0NDIwNDAxZTVhMDlmLjEwMDIiLCJhdF9oYXNoIjoiQlBiMDRqemJ2Y01IWVc2QVRwODNfdyIsImVtYWlsIjoic3llb25nZ2dnQGdtYWlsLmNvbSIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJhdXRoX3RpbWUiOjE3NTYxNTk3OTcsIm5vbmNlX3N1cHBvcnRlZCI6dHJ1ZX0.lmLrMZYCRR88AFSJ3haj2NKxonqduiL_WjvFeBsDw7TESp7tUSMEHq61pumHzDM9e0bwCvmU2-rEpYuAfuM7KKMNnIgTLT6UhBnuDAwAqT0iPrl8xkYxEAj2Xa5vvnxbNwPZ08A3Q7V8LfXpWx1F29hewOxm_opwiWguXg0QsUSVg4K9my8nHnvkmJ8zZYOa3ZTzyOtec-tqgxgePx3Bhiyrva-hxLFHTNpyDFUWLj32XVFTW_cPhsBx33aaRrmjPbcii18pqvwvD60gKwparqoGfaF0iLIApL4YUCARVd4AlbPj6hMAy0-dkfhX-DzTGAVB-5ky2cz_Y47qOGqkiQ";
    private static final String JWKS_FILE_PATH = "src/test/resources/apple-jwks.json";

    @Test
    @DisplayName("유효한 ID Token은 서명 검증에 성공한다")
    void verifyToken() throws Exception {
        // given
        String idToken = TEST_ID_TOKEN;

        // when
        SignedJWT idTokenJwt = SignedJWT.parse(idToken); // ID Token 파싱
        String jwksStr = Files.readString(Paths.get(JWKS_FILE_PATH)); // JWKs
        JWKSet jwkSet = JWKSet.parse(jwksStr); // JWKs 파싱
        JWK jwk = jwkSet.getKeyByKeyId(idTokenJwt.getHeader().getKeyID()); // ID Token 의 kid 와 일치하는 JWK 찾기
        RSAKey rsaKey = (RSAKey) jwk; // RSA 키 타입으로 변환

        // then
        boolean isValid = idTokenJwt.verify(new RSASSAVerifier(rsaKey)); // 서명 검증
        assertTrue(isValid, "유효한 ID Token은 서명 검증에 성공해야 합니다");
    }

    @Test
    @DisplayName("유효한 ID Token은 클레임 검증에 성공한다")
    void verifyClaims() throws Exception {
        // given
        String idToken = TEST_ID_TOKEN;

        // when
        SignedJWT idTokenJwt = SignedJWT.parse(idToken);
        JWTClaimsSet claims = idTokenJwt.getJWTClaimsSet();
        log.info("Issuer: {}", claims.getIssuer());
        log.info("Audience: {}", claims.getAudience().get(0));
        log.info("Expiration Time: {}", claims.getExpirationTime());

        // then
        assertAll(
            // Issuer 검증
            () -> assertEquals("https://appleid.apple.com", claims.getIssuer(),
                    "ID Token의 Issuer는 유효해야 합니다"),

            // Audience 검증
            () -> assertEquals("com.GrowIT.app.login", claims.getAudience().get(0),
                    "ID Token의 Audience는 앱 client_id와 같아야 합니다"),

            // Expiration Time 검증
            () -> assertTrue(claims.getExpirationTime().after(new Date()), 
            "ID Token의 Expiration Time은 현재 시각 이후여야 합니다")
        );
    }
}
