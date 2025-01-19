package umc.GrowIT.Server.apiPayload.exception;

import umc.GrowIT.Server.apiPayload.code.BaseErrorCode;

public class ItemHandler extends GeneralException {

    public ItemHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
