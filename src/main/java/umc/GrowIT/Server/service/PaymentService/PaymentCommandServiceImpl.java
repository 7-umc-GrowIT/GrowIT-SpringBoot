package umc.GrowIT.Server.service.PaymentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.GrowIT.Server.apiPayload.code.status.ErrorStatus;
import umc.GrowIT.Server.apiPayload.exception.PaymentHandler;
import umc.GrowIT.Server.apiPayload.exception.UserHandler;
import umc.GrowIT.Server.converter.PaymentConverter;
import umc.GrowIT.Server.domain.CreditProduct;
import umc.GrowIT.Server.domain.PaymentHistory;
import umc.GrowIT.Server.domain.User;
import umc.GrowIT.Server.repository.CreditProductRepository;
import umc.GrowIT.Server.repository.PaymentHistoryRepository;
import umc.GrowIT.Server.repository.UserRepository;
import umc.GrowIT.Server.repository.UserCreditRepository.UserCreditRepository;
import umc.GrowIT.Server.service.AppleService.AppleReceiptValidationService;
import umc.GrowIT.Server.web.dto.AppleValidationDTO.AppleValidationDTO;
import umc.GrowIT.Server.web.dto.CreditPaymentDTO.CreditPaymentRequestDTO;
import umc.GrowIT.Server.web.dto.CreditPaymentDTO.CreditPaymentResponseDTO;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PaymentCommandServiceImpl implements PaymentCommandService {

    private final UserRepository userRepository;
    private final UserCreditRepository userCreditRepository;
    private final PaymentHistoryRepository paymentHistoryRepository;
    private final CreditProductRepository creditProductRepository;
    private final AppleReceiptValidationService receiptValidationService;

    @Override
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

            // 4.  상품 정보 조회 및 검증
            CreditProduct product = creditProductRepository.findByProductId(requestDto.getProductId())
                    .orElseThrow(() -> new PaymentHandler(ErrorStatus.PAYMENT_INVALID_PRODUCT));

            // 상품 활성화 여부 확인
            if (!product.getIsActive()) {
                log.warn("비활성화된 상품: productId={}", requestDto.getProductId());
                throw new PaymentHandler(ErrorStatus.PAYMENT_INVALID_PRODUCT);
            }

            // 5. Apple Store 영수증 검증
            AppleValidationDTO appleValidationDTO =
                    receiptValidationService.getValidationDetails(requestDto);

            if (!appleValidationDTO.isValid()) {
                log.warn("영수증 검증 실패: transactionId={}, error={}",
                        requestDto.getTransactionId(), appleValidationDTO.getErrorMessage());
                throw new PaymentHandler(ErrorStatus.PAYMENT_RECEIPT_VALIDATION_FAILED);
            }

            // 6 결제 히스토리 생성 및 저장 (product 정보 사용)
            PaymentHistory paymentHistory = PaymentConverter.toPaymentHistory(userId, requestDto, product);

            // Apple 검증 결과 추가
            paymentHistory = PaymentConverter.updateWithAppleResponse(paymentHistory, appleValidationDTO);

            paymentHistoryRepository.save(paymentHistory);

            // 7. 사용자 크레딧 업데이트 (기존 UserCreditRepository 사용)
            Integer creditAmountToAdd = product.getCreditAmount();
            userCreditRepository.addCreditToUser(userId, creditAmountToAdd);

            // 8. 업데이트된 사용자 정보 다시 조회
            User updatedUser = userRepository.findById(userId)
                    .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

            log.info("크레딧 구매 성공: userId={}, credits={}, transactionId={}",
                    userId, creditAmountToAdd, requestDto.getTransactionId());

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

    @Override
    @Transactional(readOnly = true)
    public boolean validatePaymentRequest(CreditPaymentRequestDTO requestDto) {
        // 영수증 데이터 검증
        if (requestDto.getReceiptData() == null || requestDto.getReceiptData().trim().isEmpty()) {
            log.warn("영수증 데이터가 없습니다");
            return false;
        }

        // 거래 ID 검증
        if (requestDto.getTransactionId() == null || requestDto.getTransactionId().trim().isEmpty()) {
            log.warn("거래 ID가 없습니다");
            return false;
        }

        // 상품 ID 검증
        if (requestDto.getProductId() == null || requestDto.getProductId().trim().isEmpty()) {
            log.warn("상품 ID가 없습니다");
            return false;
        }

        return true;
    }
}