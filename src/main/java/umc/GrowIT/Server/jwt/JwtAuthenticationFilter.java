package umc.GrowIT.Server.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static umc.GrowIT.Server.apiPayload.code.status.ErrorStatus.*;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String token = resolveToken(request); //Authorization 헤더에서 토큰 추출

            if (token == null) {
                throw new JwtAuthenticationException(MISSING_TOKEN);
            }

            if (jwtTokenUtil.validateToken(token)) { //토큰이 제공되고, 토큰 자체가 유효한지 확인
                if (jwtTokenUtil.isUserInactive(token)) //토큰에서 사용자 정보 읽어서 탈퇴한 회원인지 확인
                    throw new JwtAuthenticationException(USER_STATUS_INACTIVE);
                Authentication authentication = jwtTokenUtil.getAuthentication(token); //토큰에서 사용자 정보 추출
                SecurityContextHolder.getContext().setAuthentication(authentication); //SecurityContext 에 사용자 정보 저장
            }

        } catch (ExpiredJwtException e) {
            request.setAttribute("errorStatus", EXPIRED_TOKEN); //CustomAuthenticationEntryPoint 에서 처리
        } catch (JwtAuthenticationException e) {
            request.setAttribute("errorStatus", e.getErrorStatus());
        }

        filterChain.doFilter(request, response); //다음 필터로 요청 전달
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer "))
            return bearerToken.substring(7); //"Bearer " 제거

        return null;
    }
}