package umc.GrowIT.Server.service.AppleService;


import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import umc.GrowIT.Server.web.dto.CreditPaymentDTO.CreditPaymentRequestDTO;

import java.util.Map;

// 임시로 구현한 Apple 영수증 검증
public class AppleReceiptValidationServiceImpl implements AppleReceiptValidationService{

    public boolean validateReceipt(String receiptData) {
        try {
            // Apple 서버에 영수증 검증 요청
            String appleUrl = "https://buy.itunes.apple.com/verifyReceipt"; // 또는 sandbox

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> request = Map.of(
                    "receipt-data", receiptData,
                    "password", "your_app_shared_secret" // App Store Connect에서 발급
            );

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

            // Apple 서버에 POST 요청
            ResponseEntity<Map> response = restTemplate.postForEntity(appleUrl, entity, Map.class);

            Map<String, Object> responseBody = response.getBody();
            Integer status = (Integer) responseBody.get("status");

            return status != null && status == 0; // 0 = 성공

        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean validateReceipt(CreditPaymentRequestDTO requestDTO) {
        return false;
    }

    @Override
    public AppleValidationResponse getValidationDetails(CreditPaymentRequestDTO requestDTO) {
        return null;
    }
}
