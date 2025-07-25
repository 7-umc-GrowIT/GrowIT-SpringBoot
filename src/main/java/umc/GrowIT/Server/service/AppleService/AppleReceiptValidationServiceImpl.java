package umc.GrowIT.Server.service.AppleService;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import umc.GrowIT.Server.service.AppleService.AppleReceiptValidationService;
import umc.GrowIT.Server.web.dto.AppleValidationDTO.AppleValidationDTO;
import umc.GrowIT.Server.web.dto.CreditPaymentDTO.CreditPaymentRequestDTO;

import java.util.Map;

@Service                // Spring Bean으로 등록
@RequiredArgsConstructor // final 필드들에 대한 생성자 자동 생성
@Slf4j                  // 로깅을 위한 Lombok 어노테이션
public class AppleReceiptValidationServiceImpl implements AppleReceiptValidationService {

    // RestTemplate: HTTP 요청을 보내기 위한 Spring 클래스 (의존성 주입)
    private final RestTemplate restTemplate;

    // JSON 파싱을 위한 Jackson ObjectMapper (의존성 주입)
    private final ObjectMapper objectMapper;

    // application.yml에서 설정값들을 주입받음
    @Value("${apple.receipt.validation.url.production:https://buy.itunes.apple.com/verifyReceipt}")
    private String productionUrl;  // Apple 운영환경 검증 URL

    @Value("${apple.receipt.validation.url.sandbox:https://sandbox.itunes.apple.com/verifyReceipt}")
    private String sandboxUrl;     // Apple 테스트환경 검증 URL

    @Value("${apple.receipt.shared.secret:}")
    private String sharedSecret;   // App Store Connect에서 발급받은 공유 비밀키

    @Value("${apple.bundle.id:com.example.growit}")
    private String expectedBundleId; // 우리 앱의 번들 ID (보안 검증용)

    /**
     * 영수증 검증 (단순 성공/실패만 반환)
     */
    @Override
    public boolean validateReceipt(CreditPaymentRequestDTO requestDto) {
        // 상세 검증 결과를 가져와서 성공 여부만 반환
        AppleValidationDTO response = getValidationDetails(requestDto);
        return response.isValid();
    }

    /**
     * 영수증 검증 + 상세 정보 반환 (메인 로직)
     */
    @Override
    public AppleValidationDTO getValidationDetails(CreditPaymentRequestDTO requestDto) {
        try {
            log.info("Apple 영수증 검증 시작: transactionId={}", requestDto.getTransactionId());

            // 1단계: 먼저 Production 환경에서 검증 시도
            AppleValidationDTO productionResponse = callAppleAPI(productionUrl, requestDto.getReceiptData());

            // 2단계: Production에서 21007 에러(Sandbox 영수증)가 나오면 Sandbox로 재시도
            if (!productionResponse.isValid() && isSandboxReceiptError(productionResponse.getFullResponse())) {
                log.info("Production 검증 실패 (Sandbox 영수증), Sandbox로 재시도");
                return callAppleAPI(sandboxUrl, requestDto.getReceiptData());
            }

            return productionResponse;

        } catch (Exception e) {
            // 네트워크 오류 등 예상치 못한 오류 처리
            log.error("Apple 영수증 검증 중 오류 발생", e);
            return AppleValidationDTO.failure(
                    "네트워크 오류: " + e.getMessage(),
                    "{\"error\": \"" + e.getMessage() + "\"}"
            );
        }
    }

    /**
     * Apple API 실제 호출 (HTTP POST 요청)
     */
    private AppleValidationDTO callAppleAPI(String url, String receiptData) {
        try {
            // HTTP 헤더 설정 (JSON 형식으로 요청)
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Apple API 요청 바디 생성
            Map<String, Object> requestBody = Map.of(
                    "receipt-data", receiptData,    // Base64 인코딩된 영수증 데이터
                    "password", sharedSecret        // App Store Connect 공유 비밀키
            );

            // HTTP 요청 엔티티 생성 (헤더 + 바디)
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            // Apple 서버에 POST 요청 전송
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

            // Apple 응답 JSON 파싱 및 검증
            return parseAppleResponse(response.getBody());

        } catch (Exception e) {
            // HTTP 요청 실패 시 처리
            log.error("Apple API 호출 실패: url={}", url, e);
            return AppleValidationDTO.failure(
                    "Apple API 호출 실패: " + e.getMessage(),
                    "{\"error\": \"" + e.getMessage() + "\"}"
            );
        }
    }

    /**
     * Apple 응답 JSON 파싱 및 검증
     */
    private AppleValidationDTO parseAppleResponse(String responseJson) {
        try {
            // JSON 문자열을 JsonNode 객체로 파싱
            JsonNode root = objectMapper.readTree(responseJson);
            int status = root.get("status").asInt();

            // Apple status 코드 확인 (0 = 성공, 그외 = 실패)
            if (status == 0) {
                // 성공 시: 거래 정보 추출
                return parseSuccessResponse(root, responseJson);
            } else {
                // 실패 시: 에러 응답 반환
                return AppleValidationDTO.failure(
                        "Apple 검증 실패: status=" + status,
                        responseJson
                );
            }

        } catch (Exception e) {
            // JSON 파싱 실패 시 처리
            log.error("Apple 응답 파싱 실패", e);
            return AppleValidationDTO.failure(
                    "응답 파싱 실패: " + e.getMessage(),
                    responseJson
            );
        }
    }

    /**
     * 성공 응답에서 필요한 거래 정보 추출
     */
    private AppleValidationDTO parseSuccessResponse(JsonNode root, String fullResponse) {
        try {
            // receipt 정보 추출
            JsonNode receipt = root.get("receipt");
            String bundleId = receipt.get("bundle_id").asText();      // 앱 번들 ID
            String environment = root.get("environment").asText();    // Sandbox/Production

            // 보안 검증: 번들 ID가 우리 앱인지 확인
            if (!expectedBundleId.equals(bundleId)) {
                return AppleValidationDTO.failure(
                        "번들 ID 불일치: 예상=" + expectedBundleId + ", 실제=" + bundleId,
                        fullResponse
                );
            }

            // in_app 배열에서 거래 정보 추출 (인앱구매 내역)
            JsonNode inAppArray = receipt.get("in_app");
            if (inAppArray != null && inAppArray.isArray() && inAppArray.size() > 0) {
                // 첫 번째 거래 정보 (가장 최근 거래)
                JsonNode firstTransaction = inAppArray.get(0);
                String originalTransactionId = firstTransaction.get("original_transaction_id").asText();
                String purchaseDate = firstTransaction.get("purchase_date").asText();

                // 성공 응답 객체 생성
                return AppleValidationDTO.success(
                        originalTransactionId,  // Apple 원본 거래 ID
                        purchaseDate,          // 구매 일시
                        bundleId,              // 앱 번들 ID
                        environment,           // 환경 (Sandbox/Production)
                        fullResponse           // 전체 응답 JSON (로깅용)
                );
            } else {
                // 거래 정보가 없는 경우
                return AppleValidationDTO.failure(
                        "거래 정보가 없습니다",
                        fullResponse
                );
            }

        } catch (Exception e) {
            // 성공 응답 파싱 실패
            log.error("성공 응답 파싱 실패", e);
            return AppleValidationDTO.failure(
                    "성공 응답 파싱 실패: " + e.getMessage(),
                    fullResponse
            );
        }
    }

    /**
     * 21007 에러 확인 (Production 서버에 Sandbox 영수증 전송 시 발생)
     */
    private boolean isSandboxReceiptError(String responseJson) {
        try {
            JsonNode root = objectMapper.readTree(responseJson);
            int status = root.get("status").asInt();
            return status == 21007; // 21007 => 테스트 환경임을 의미하는 status
        } catch (Exception e) {
            return false;
        }
    }
}