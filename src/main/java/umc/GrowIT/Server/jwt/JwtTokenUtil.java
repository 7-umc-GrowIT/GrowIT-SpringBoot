package umc.GrowIT.Server.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import umc.GrowIT.Server.service.userService.UserQueryService;
import umc.GrowIT.Server.web.dto.UserDTO.UserResponseDTO;

import java.security.Key;
import java.util.Collections;
import java.util.Date;
import java.util.stream.Collectors;

import static umc.GrowIT.Server.converter.TokenConverter.toTokenDTO;


//JWT 토큰을 생성하고 검증하는 유틸리티 클래스
@Component
public class JwtTokenUtil {

    private final UserQueryService userQueryService;

    private final Key key;
    public static final long ACCESS_TOKEN_EXPIRATION_MS = 30L * 24 * 60 * 60 * 1000; //테스트 용 Access token 만료 시간 30일
    public static final long REFRESH_TOKEN_EXPIRATION_MS = 60L * 24 * 60 * 60 * 1000; //테스트 용 Refresh token 만료 시간 60일

    public JwtTokenUtil(@Value("${spring.jwt.secretKey}") String secretKey, UserQueryService userQueryService) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.userQueryService = userQueryService;
    }

    // AccessToken, RefreshToken 생성 메소드 호출
    public UserResponseDTO.TokenDTO generateToken(CustomUserDetails customUserDetails) {
        String accessToken = generateAccessToken(customUserDetails);
        String refreshToken = generateRefreshToken(customUserDetails);

        return toTokenDTO(accessToken, refreshToken);
    }

    public String generateAccessToken(CustomUserDetails customUserDetails) {
        String authorities = customUserDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();

        // Access Token 생성
        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRATION_MS);
        String accessToken = Jwts.builder()
                .setSubject(customUserDetails.getUsername())
                .claim("roles", authorities)
                .claim("userId", customUserDetails.getId())
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return accessToken;
    }

    public String generateRefreshToken(CustomUserDetails customUserDetails) {
        long now = (new Date()).getTime();

        // Refresh Token 생성
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + REFRESH_TOKEN_EXPIRATION_MS))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return refreshToken;
    }

    //JWT 토큰에서 Claim 추출
    public Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    //토큰 유효성 검증
    public boolean validateToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token) //예외 발생 시 예외 던짐
                .getBody();
        return true;
    }

    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);
        Long userId = claims.get("userId", Long.class);
        String roles = claims.get("roles", String.class);

        return new UsernamePasswordAuthenticationToken(
                userId,
                null,
                Collections.singletonList(new SimpleGrantedAuthority(roles))
        );
    }

    public boolean isUserInactive(String token){
        Claims claims = parseClaims(token);
        Long userId = claims.get("userId", Long.class);
        return userQueryService.isUserInactive(userId);
    }
}
