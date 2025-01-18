package umc.GrowIT.Server.apiPayload.exception;

import umc.GrowIT.Server.apiPayload.code.BaseErrorCode;

public class ChallengeHandler extends GeneralException {

    public ChallengeHandler(BaseErrorCode errorCode) { super(errorCode); }
}
