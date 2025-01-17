package umc.GrowIT.Server.service.refreshTokenService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import umc.GrowIT.Server.converter.TokenConverter;
import umc.GrowIT.Server.domain.RefreshToken;
import umc.GrowIT.Server.domain.User;
<<<<<<< HEAD:src/main/java/umc/GrowIT/Server/service/refreshTokenService/RefreshTokenCommandServiceImpl.java
import umc.GrowIT.Server.jwt.JwtTokenUtil;
=======
import umc.GrowIT.Server.jwt.JwtTokenProvider;
>>>>>>> 8bc95fed222fc14c8e376329716b6baa92994c03:src/main/java/umc/GrowIT/Server/service/refreshToken/RefreshTokenCommandServiceImpl.java
import umc.GrowIT.Server.repository.RefreshTokenRepository;
import umc.GrowIT.Server.service.refreshToken.RefreshTokenCommandService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class RefreshTokenCommandServiceImpl implements RefreshTokenCommandService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public RefreshToken createRefreshToken(String refreshToken, User user) {
        Date expiryDate = jwtTokenUtil.parseClaims(refreshToken).getExpiration();

        LocalDateTime localDateTime = LocalDateTime.ofInstant(expiryDate.toInstant(), ZoneId.systemDefault());

        RefreshToken refreshTokenEntity = TokenConverter.toRefreshToken(refreshToken, localDateTime, user);

        return refreshTokenRepository.save(refreshTokenEntity);
    }

}
