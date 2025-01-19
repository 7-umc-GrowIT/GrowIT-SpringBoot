package umc.GrowIT.Server.apiPayload.exception;

import umc.GrowIT.Server.apiPayload.code.BaseErrorCode;

public class DiaryHandler extends GeneralException{
    public DiaryHandler(BaseErrorCode errorcode) {
        super(errorcode);
    }
}
