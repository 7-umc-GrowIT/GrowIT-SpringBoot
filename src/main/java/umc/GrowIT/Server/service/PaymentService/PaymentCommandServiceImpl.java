package umc.GrowIT.Server.service.PaymentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.GrowIT.Server.apiPayload.code.status.ErrorStatus;
import umc.GrowIT.Server.apiPayload.exception.PaymentHandler;
import umc.GrowIT.Server.apiPayload.exception.UserHandler;
import umc.GrowIT.Server.converter.PaymentConverter;
import umc.GrowIT.Server.domain.PaymentHistory;
import umc.GrowIT.Server.domain.User;
import umc.GrowIT.Server.repository.UserCreditRepository.UserCreditRepository;
import umc.GrowIT.Server.repository.PaymentHistoryRepository;
import umc.GrowIT.Server.repository.UserRepository;
import umc.GrowIT.Server.service.AppleService.AppleReceiptValidationService;
import umc.GrowIT.Server.service.CreditProductService.CreditProductQueryService;
import umc.GrowIT.Server.web.dto.AppleValidationDTO.AppleValidationDTO;
import umc.GrowIT.Server.web.dto.CreditPaymentDTO.CreditPaymentRequestDTO;
import umc.GrowIT.Server.web.dto.CreditPaymentDTO.CreditPaymentResponseDTO;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentCommandServiceImpl implements PaymentCommandService {

    private final UserRepository userRepository;
    private final UserCreditRepository userCreditRepository;
    private final PaymentHistoryRepository paymentHistoryRepository;
    private final CreditProductQueryService creditProductQueryService;
    private final AppleReceiptValidationService receiptValidationService;

    @Override
    @Transactional
    public CreditPaymentResponseDTO processCreditPayment(Long userId, CreditPaymentRequestDTO requestDto) {
        try {
            log.info("크레딧 구매 처리 시작: userId={}, transactionId={}", userId, requestDto.getTransactionId());

            // 1. 기본 요청 데이터 검증
            if (!validatePaymentRequest(requestDto)) {
                log.warn("요청 데이터 검증 실패: userId={}", userId);
                throw new PaymentHandler(ErrorStatus.PAYMENT_INVALID_REQUEST);
            }

            // 2. 사용자 조회
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

            // 3. 중복 거래 확인
            if (paymentHistoryRepository.existsByTransactionId(requestDto.getTransactionId())) {
                log.warn("중복 거래 시도: transactionId={}", requestDto.getTransactionId());
                throw new PaymentHandler(ErrorStatus.PAYMENT_DUPLICATE_TRANSACTION);
            }

            // 4. 상품 정보 검증
            if (!creditProductQueryService.validateProduct(
                    requestDto.getProductId(),
                    requestDto.getCreditAmount(),
                    requestDto.getAmount())) {
                log.warn("상품 정보 검증 실패: productId={}", requestDto.getProductId());
                throw new PaymentHandler(ErrorStatus.PAYMENT_INVALID_PRODUCT);
            }

            // 5. Apple Store 영수증 검증
            AppleValidationDTO AppleValidationDTO =
                    receiptValidationService.getValidationDetails(requestDto);

            if (!AppleValidationDTO.isValid()) {
                log.warn("영수증 검증 실패: transactionId={}, error={}",
                        requestDto.getTransactionId(), AppleValidationDTO.getErrorMessage());
                throw new PaymentHandler(ErrorStatus.PAYMENT_RECEIPT_VALIDATION_FAILED);
            }

            // 6. 결제 히스토리 생성 및 저장
            PaymentHistory paymentHistory = PaymentConverter.toPaymentHistory(userId, requestDto);

            // Apple 검증 결과 추가
            paymentHistory = PaymentConverter.updateWithAppleResponse(paymentHistory, AppleValidationDTO);

            paymentHistoryRepository.save(paymentHistory);

            // 7. 사용자 크레딧 업데이트
            userCreditRepository.addCreditToUser(userId, requestDto.getCreditAmount());

            // 8. 업데이트된 사용자 정보 다시 조회
            User updatedUser = userRepository.findById(userId)
                    .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

            log.info("크레딧 구매 성공: userId={}, credits={}, transactionId={}",
                    userId, requestDto.getCreditAmount(), requestDto.getTransactionId());

            return PaymentConverter.toSuccessResponse(paymentHistory, updatedUser);

        } catch (PaymentHandler | UserHandler e) {
            // 비즈니스 로직 예외는 그대로 던짐
            log.error("크레딧 구매 처리 실패: userId={}, transactionId={}, error={}",
                    userId, requestDto.getTransactionId(), e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("크레딧 구매 처리 중 예상치 못한 오류 발생: userId={}, transactionId={}",
                    userId, requestDto.getTransactionId(), e);

            throw new PaymentHandler(ErrorStatus.PAYMENT_PROCESSING_ERROR);
        }
    }

    @Override // apple store와 비교하여 검증
    @Transactional(readOnly = true)
    public boolean validatePaymentRequest(CreditPaymentRequestDTO requestDto){

        return true;
    }

}
