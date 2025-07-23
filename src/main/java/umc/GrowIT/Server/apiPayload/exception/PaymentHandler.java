package umc.GrowIT.Server.apiPayload.exception;

import umc.GrowIT.Server.apiPayload.code.BaseErrorCode;

public class PaymentHandler extends GeneralException{
    public PaymentHandler(BaseErrorCode code) {
        super(code);
    }
}
