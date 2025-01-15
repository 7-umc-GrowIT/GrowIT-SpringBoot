package umc.GrowIT.Server.converter;

import umc.GrowIT.Server.domain.RefreshToken;

import java.time.LocalDateTime;

public class TokenConverter {

    public static RefreshToken toRefreshToken(String refreshToken, LocalDateTime expiryDate) {

        return RefreshToken.builder()
                .refreshToken(refreshToken)
                .expiryDate(expiryDate)
                .build();
    }

}
