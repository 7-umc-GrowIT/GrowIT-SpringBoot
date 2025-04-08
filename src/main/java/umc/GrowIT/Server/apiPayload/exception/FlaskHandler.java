package umc.GrowIT.Server.apiPayload.exception;

import umc.GrowIT.Server.apiPayload.code.BaseErrorCode;

public class FlaskHandler extends GeneralException {
    public FlaskHandler(BaseErrorCode code) {
        super(code);
    }

}
