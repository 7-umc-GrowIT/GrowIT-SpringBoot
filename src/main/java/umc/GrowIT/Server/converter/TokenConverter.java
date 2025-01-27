package umc.GrowIT.Server.converter;

import umc.GrowIT.Server.domain.RefreshToken;
import umc.GrowIT.Server.domain.User;
import umc.GrowIT.Server.web.dto.UserDTO.UserResponseDTO;

import java.time.LocalDateTime;

public class TokenConverter {

    public static RefreshToken toRefreshToken(String refreshToken, LocalDateTime expiryDate, User user) {
        return RefreshToken.builder()
                .refreshToken(refreshToken)
                .expiryDate(expiryDate)
                .user(user)
                .build();
    }

    public static UserResponseDTO.AccessTokenDTO toAccessTokenDTO(String accessToken) {
        return UserResponseDTO.AccessTokenDTO.builder()
                .accessToken(accessToken)
                .build();
    }

    public static UserResponseDTO.TokenDTO toTokenDTO(String accessToken, String refreshToken){
        return UserResponseDTO.TokenDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

}
