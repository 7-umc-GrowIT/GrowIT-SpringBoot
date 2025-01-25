package umc.GrowIT.Server.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Collections;
import java.util.Date;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import umc.GrowIT.Server.domain.CustomUserDetails;
import umc.GrowIT.Server.service.authService.CustomUserDetailsService;
import umc.GrowIT.Server.web.dto.UserDTO.UserResponseDTO;

import static umc.GrowIT.Server.converter.TokenConverter.toTokenDTO;


//JWT 토큰을 생성하고 검증하는 유틸리티 클래스
@Component
public class JwtTokenUtil {


    private final Key key;
    public static final long ACCESS_TOKEN_EXPIRATION_MS = 60000; //12L * 60 * 60 * 1000; //Access token 만료 시간 12시간
    public static final long REFRESH_TOKEN_EXPIRATION_MS = 120000; //30L * 24 * 60 * 60 * 1000; //Refresh token 만료 시간 30일

    public JwtTokenUtil(@Value("${spring.jwt.secretKey}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
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
                .claim("id", customUserDetails.getId())
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
                .parseClaimsJws(token)
                .getBody();

        return true;
    }

    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);
        Long id = claims.get("id", Long.class);
        String roles = claims.get("roles", String.class);

        return new UsernamePasswordAuthenticationToken(
                id,
                null,
                Collections.singletonList(new SimpleGrantedAuthority(roles))
        );
    }
}
