package umc.GrowIT.Server.apiPayload.exception;

import umc.GrowIT.Server.apiPayload.code.BaseErrorCode;

public class OAuthHandler extends GeneralException {
    public OAuthHandler(BaseErrorCode code) {
        super(code);
    }
}