package umc.GrowIT.Server.converter;

import umc.GrowIT.Server.domain.RefreshToken;
import umc.GrowIT.Server.domain.Term;
import umc.GrowIT.Server.dto.TermResponseDTO;

import java.time.LocalDateTime;

public class TokenConverter {

    public static RefreshToken toRefreshToken(String refreshToken, LocalDateTime expiryDate) {

        return RefreshToken.builder()
                .refreshToken(refreshToken)
                .expiryDate(expiryDate)
                .build();
    }

}
