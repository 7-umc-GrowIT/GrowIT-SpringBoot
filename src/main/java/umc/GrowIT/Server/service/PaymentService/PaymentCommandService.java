package umc.GrowIT.Server.service.PaymentService;

import umc.GrowIT.Server.web.dto.CreditPaymentDTO.CreditPaymentRequestDTO;
import umc.GrowIT.Server.web.dto.CreditPaymentDTO.CreditPaymentResponseDTO;

public interface PaymentCommandService {
    
    //크레딧 구매 처리
    public CreditPaymentResponseDTO processCreditPayment(Long userId, CreditPaymentRequestDTO requestDto);

    public boolean validatePaymentRequest(CreditPaymentRequestDTO requestDto);
}
