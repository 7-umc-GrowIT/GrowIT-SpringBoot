package umc.GrowIT.Server.service.AppleService;

import umc.GrowIT.Server.web.dto.AppleValidationDTO.AppleValidationDTO;
import umc.GrowIT.Server.web.dto.CreditPaymentDTO.CreditPaymentRequestDTO;


public interface AppleReceiptValidationService {

    // 검증 성공 여부 판정
    boolean validateReceipt(CreditPaymentRequestDTO requestDTO);

    // AppStore 영수증 검증 api 응답에서 상세 정보 추출
    AppleValidationDTO getValidationDetails(CreditPaymentRequestDTO requestDTO);





}
