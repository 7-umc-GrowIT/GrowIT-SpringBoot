package umc.GrowIT.Server.apiPayload.exception;

import umc.GrowIT.Server.apiPayload.code.BaseErrorCode;

public class ImageHandler extends GeneralException {
    public ImageHandler(BaseErrorCode code) {
        super(code);
    }
}