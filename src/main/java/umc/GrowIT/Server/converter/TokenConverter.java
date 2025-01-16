package umc.GrowIT.Server.converter;

import umc.GrowIT.Server.domain.RefreshToken;
import umc.GrowIT.Server.domain.User;

import java.time.LocalDateTime;

public class TokenConverter {

    public static RefreshToken toRefreshToken(String refreshToken, LocalDateTime expiryDate, User user) {

        return RefreshToken.builder()
                .refreshToken(refreshToken)
                .expiryDate(expiryDate)
                .user(user)
                .build();
    }

}
