package umc.GrowIT.Server.apiPayload.exception;

import umc.GrowIT.Server.apiPayload.code.BaseErrorCode;

public class UserChallengeHandler extends GeneralException {
    public UserChallengeHandler(BaseErrorCode code) {
        super(code);
    }
}
