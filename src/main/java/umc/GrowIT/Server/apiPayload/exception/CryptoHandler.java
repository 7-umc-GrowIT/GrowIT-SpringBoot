package umc.GrowIT.Server.apiPayload.exception;

import umc.GrowIT.Server.apiPayload.code.BaseErrorCode;

public class CryptoHandler extends GeneralException {
    public CryptoHandler(BaseErrorCode code) {
        super(code);
    }
}
