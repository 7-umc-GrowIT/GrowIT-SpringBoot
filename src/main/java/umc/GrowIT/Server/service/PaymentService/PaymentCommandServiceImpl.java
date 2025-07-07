package umc.GrowIT.Server.service.PaymentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.GrowIT.Server.web.dto.CreditPaymentDTO.CreditPaymentRequestDTO;
import umc.GrowIT.Server.web.dto.CreditPaymentDTO.CreditPaymentResponseDTO;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentCommandServiceImpl implements PaymentCommandService {

    @Override // 크레딧 구매처리
    public CreditPaymentResponseDTO processCreditPayment(Long userId, CreditPaymentRequestDTO requestDto){

        return null;
    }

    @Override // apple store와 비교하여 검증
    @Transactional(readOnly = true)
    public boolean validatePaymentRequest(CreditPaymentRequestDTO requestDto){

        return true;
    }

}
