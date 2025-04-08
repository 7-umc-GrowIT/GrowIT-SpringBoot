package umc.GrowIT.Server.apiPayload.exception;

import umc.GrowIT.Server.apiPayload.code.BaseErrorCode;

public class GroHandler extends GeneralException{
    public GroHandler(BaseErrorCode code) {
        super(code);
    }
}
