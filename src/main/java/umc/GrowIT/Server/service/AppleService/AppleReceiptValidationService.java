package umc.GrowIT.Server.service.AppleService;

import lombok.*;
import umc.GrowIT.Server.web.dto.CreditPaymentDTO.CreditPaymentRequestDTO;

public interface AppleReceiptValidationService {

    // 검증 성공 여부 판정
    boolean validateReceipt(CreditPaymentRequestDTO requestDTO);

    // AppStore 영수증 검증 api 응답에서 상세 정보 추출
    AppleValidationResponse getValidationDetails(CreditPaymentRequestDTO requestDTO);



    @AllArgsConstructor
    @Getter
    class AppleValidationResponse {
        private final boolean isValid;
        private final String originalTransactionId;
        private final String purchaseDate;
        private final String bundleId;
        private final String environment;
        private final String fullResponse;
        private final String errorMessage;


        // 성공 응답 생성
        public static AppleValidationResponse success(String originalTransactionId, String purchaseDate,
                                                      String bundleId, String environment, String fullResponse) {
            return new AppleValidationResponse(true, originalTransactionId, purchaseDate,
                    bundleId, environment, fullResponse, null);
        }

        // 실패 응답 생성
        public static AppleValidationResponse failure(String errorMessage, String fullResponse) {
            return new AppleValidationResponse(false, null, null, null, null, fullResponse, errorMessage);
        }
    }

}
