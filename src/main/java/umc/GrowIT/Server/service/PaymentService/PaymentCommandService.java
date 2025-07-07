package umc.GrowIT.Server.service.PaymentService;

import umc.GrowIT.Server.web.dto.CreditPaymentDTO.CreditPaymentRequestDTO;
import umc.GrowIT.Server.web.dto.CreditPaymentDTO.CreditPaymentResponseDTO;

public interface PaymentCommandService {


    public CreditPaymentResponseDTO processCreditPayment(Long userId, CreditPaymentRequestDTO requestDto);
}
