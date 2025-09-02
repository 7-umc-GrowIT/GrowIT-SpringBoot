package umc.GrowIT.Server.domain.enums;

import static umc.GrowIT.Server.apiPayload.code.status.ErrorStatus.INVALID_PROVIDER;

import umc.GrowIT.Server.apiPayload.exception.OAuthHandler;

public enum Provider {
    APPLE,
    KAKAO;

    public static Provider from(String provider) {
        try {
            return Provider.valueOf(provider.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new OAuthHandler(INVALID_PROVIDER);
        }
    }
}
