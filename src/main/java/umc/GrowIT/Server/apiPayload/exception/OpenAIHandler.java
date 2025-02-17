package umc.GrowIT.Server.apiPayload.exception;

import umc.GrowIT.Server.apiPayload.code.BaseErrorCode;

public class OpenAIHandler extends GeneralException {
    public OpenAIHandler(BaseErrorCode code) {
        super(code);
    }

}
