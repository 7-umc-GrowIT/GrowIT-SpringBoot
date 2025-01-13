package umc.GrowIT.Server.service.refreshToken;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import umc.GrowIT.Server.converter.TokenConverter;
import umc.GrowIT.Server.domain.RefreshToken;
import umc.GrowIT.Server.jwt.JwtTokenProvider;
import umc.GrowIT.Server.repository.RefreshTokenRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class RefreshTokenCommandServiceImpl implements RefreshTokenCommandService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void createRefreshToken(String refreshToken) {
        Date expiryDate = jwtTokenProvider.parseClaims(refreshToken).getExpiration();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(expiryDate.toInstant(), ZoneId.systemDefault());

        RefreshToken refreshTokenEntity = TokenConverter.toRefreshToken(refreshToken, localDateTime);

        refreshTokenRepository.save(refreshTokenEntity);
    }

}
