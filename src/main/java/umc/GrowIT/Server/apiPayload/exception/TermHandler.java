package umc.GrowIT.Server.apiPayload.exception;

import umc.GrowIT.Server.apiPayload.code.BaseErrorCode;

public class TermHandler extends GeneralException {
    public TermHandler(BaseErrorCode code) {
        super(code);
    }
}
