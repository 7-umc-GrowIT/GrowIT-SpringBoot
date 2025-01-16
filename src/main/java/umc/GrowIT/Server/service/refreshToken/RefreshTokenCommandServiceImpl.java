package umc.GrowIT.Server.service.refreshTokenService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import umc.GrowIT.Server.converter.TokenConverter;
import umc.GrowIT.Server.domain.RefreshToken;
import umc.GrowIT.Server.domain.User;
import umc.GrowIT.Server.jwt.JwtTokenProvider;
import umc.GrowIT.Server.repository.RefreshTokenRepository;
import umc.GrowIT.Server.service.refreshToken.RefreshTokenCommandService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class RefreshTokenCommandServiceImpl implements RefreshTokenCommandService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public RefreshToken createRefreshToken(String refreshToken, User user) {
        Date expiryDate = jwtTokenProvider.parseClaims(refreshToken).getExpiration();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(expiryDate.toInstant(), ZoneId.systemDefault());

        RefreshToken refreshTokenEntity = TokenConverter.toRefreshToken(refreshToken, localDateTime, user);

        return refreshTokenRepository.save(refreshTokenEntity);
    }

}
