package umc.GrowIT.Server.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import umc.GrowIT.Server.jwt.JwtTokenProvider;

import java.util.Base64;

@RestController
@RequiredArgsConstructor
@Slf4j // 로그 라이브러리 사용
public class TestController {

    private final JwtTokenProvider jwtTokenProvider;
    public String accessToken;

    @GetMapping("/test")
    public String test(@RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        // 요청 헤더에서 Authorization 값 추출
        accessToken = authorizationHeader.split(" ")[1];
        log.info("Access Token: {}", accessToken); // 토큰 값 로깅

        // JWT Claims에서 이메일 추출
        String email = jwtTokenProvider.parseClaims(accessToken).getSubject();
        log.info("Parsed Email: {}", email); // 이메일 값 로깅

        // JWT 토큰 디코딩 (Header, Payload, Signature)
        String header = new String(Base64.getDecoder().decode(accessToken.split("\\.")[0])); // Header 디코딩
        String payload = new String(Base64.getDecoder().decode(accessToken.split("\\.")[1])); // Payload 디코딩
        String signature = accessToken.split("\\.")[2]; // Signature는 디코딩하지 않음


        // 디코딩 결과 로그 출력
        log.info("JWT Header: {}", header);
        log.info("JWT Payload: {}", payload);
        log.info("JWT Signature: {}", signature);

        // 토큰 유효성 검사
        if (jwtTokenProvider.validateToken(accessToken)) {
            log.info("Token is valid. Login successful.");
            return "로그인 성공";
        }

        log.warn("Token validation failed. Login failed.");
        return "로그인 실패";
    }
}
