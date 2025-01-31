package umc.GrowIT.Server.service.refreshTokenService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.GrowIT.Server.apiPayload.code.status.ErrorStatus;
import umc.GrowIT.Server.apiPayload.exception.AuthHandler;
import umc.GrowIT.Server.apiPayload.exception.UserHandler;
import umc.GrowIT.Server.converter.TokenConverter;
import umc.GrowIT.Server.jwt.CustomUserDetails;
import umc.GrowIT.Server.domain.RefreshToken;
import umc.GrowIT.Server.domain.User;
import umc.GrowIT.Server.jwt.JwtTokenUtil;
import umc.GrowIT.Server.repository.RefreshTokenRepository;
import umc.GrowIT.Server.jwt.CustomUserDetailsService;
import umc.GrowIT.Server.web.dto.UserDTO.UserRequestDTO;
import umc.GrowIT.Server.web.dto.UserDTO.UserResponseDTO;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static umc.GrowIT.Server.converter.TokenConverter.toAccessTokenDTO;
import static umc.GrowIT.Server.domain.enums.UserStatus.INACTIVE;

@Service
@RequiredArgsConstructor
public class RefreshTokenCommandServiceImpl implements RefreshTokenCommandService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    public RefreshToken createRefreshToken(String refreshToken, User user) {
        Date expiryDate = jwtTokenUtil.parseClaims(refreshToken).getExpiration();

        LocalDateTime localDateTime = LocalDateTime.ofInstant(expiryDate.toInstant(), ZoneId.systemDefault());

        RefreshToken refreshTokenEntity = TokenConverter.toRefreshToken(refreshToken, localDateTime, user);

        return refreshTokenRepository.save(refreshTokenEntity);
    }

    @Transactional(noRollbackFor = AuthHandler.class)
    @Override
    public UserResponseDTO.AccessTokenDTO reissueToken(UserRequestDTO.ReissueDTO reissueDTO) {
        RefreshToken storedRefreshToken = refreshTokenRepository.findByRefreshToken(reissueDTO.getRefreshToken()) //요청한 refresh token 이 database 에 존재하는지 확인
                .orElseThrow(() -> new AuthHandler(ErrorStatus.REFRESH_TOKEN_NOT_FOUND));

        if (storedRefreshToken.getUser().getStatus() == INACTIVE)   //탈퇴한 회원은 accessToken 재발급 하지 않음
            throw new UserHandler(ErrorStatus.USER_STATUS_INACTIVE);

        if (storedRefreshToken.getExpiryDate().isBefore(LocalDateTime.now())) { //refresh token 이 만료되었는지 확인
            storedRefreshToken.getUser().deleteRefreshToken();
            throw new AuthHandler(ErrorStatus.EXPIRED_TOKEN);
        }

        CustomUserDetails customUserDetails = customUserDetailsService.loadUserByUsername(storedRefreshToken.getUser().getEmail());

        String accessToken = jwtTokenUtil.generateAccessToken(customUserDetails); //액세스 토큰 생성

        return toAccessTokenDTO(accessToken);

    }

}
