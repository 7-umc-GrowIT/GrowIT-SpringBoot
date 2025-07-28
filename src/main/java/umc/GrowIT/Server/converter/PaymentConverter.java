package umc.GrowIT.Server.converter;

import jakarta.persistence.Converter;
import umc.GrowIT.Server.domain.CreditProduct;
import umc.GrowIT.Server.domain.PaymentHistory;
import umc.GrowIT.Server.domain.User;
import umc.GrowIT.Server.domain.enums.PaymentStatus;
import umc.GrowIT.Server.service.AppleService.AppleReceiptValidationService;
import umc.GrowIT.Server.web.dto.AppleValidationDTO.AppleValidationDTO;
import umc.GrowIT.Server.web.dto.CreditPaymentDTO.CreditPaymentRequestDTO;
import umc.GrowIT.Server.web.dto.CreditPaymentDTO.CreditPaymentResponseDTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class PaymentConverter {

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    //결제 성공시 응답
    public static CreditPaymentResponseDTO toSuccessResponse(PaymentHistory paymentHistory, User user){

        return CreditPaymentResponseDTO.builder()
                .currentCredit(user.getCurrentCredit())
                .totalCredit(user.getTotalCredit())
                .status("Success")
                .paidAt(formatDateTime(paymentHistory.getCreatedAt()))
                .build();
    }

    //결제 실패시 응답
    public static CreditPaymentResponseDTO toFailureResponse(String reason){
        return CreditPaymentResponseDTO.builder()
                .currentCredit(null)   // 변하지않았으므로 크레딧값 불필요
                .totalCredit(null)
                .status("FAILED")
                .paidAt(formatDateTime(LocalDateTime.now()))
                .build();
    }

    // 결제 대기 응답
    public static CreditPaymentResponseDTO toPendingResponse() {
        return CreditPaymentResponseDTO.builder()
                .currentCredit(null)
                .totalCredit(null)
                .status("PENDING")
                .paidAt(formatDateTime(LocalDateTime.now()))
                .build();
    }

    // 결제내용 저장에 사용할 형태
    // 결제 요청 데이터 -> PaymentHistory 형태로 저장
    public static PaymentHistory toPaymentHistory(Long userId, CreditPaymentRequestDTO requestDto, CreditProduct product){

        PaymentHistory paymentHistory = new PaymentHistory();

        paymentHistory.setUserId(userId);  // Long 타입 그대로 사용
        paymentHistory.setTransactionId(requestDto.getTransactionId());
        paymentHistory.setProductId(requestDto.getProductId());
        paymentHistory.setCreditAmount(product.getCreditAmount());
        paymentHistory.setPaymentAmount(product.getPrice());
        paymentHistory.setPlatform("iOS");
        paymentHistory.setReceiptData(requestDto.getReceiptData());
        paymentHistory.setStatus(PaymentStatus.PENDING);

        return paymentHistory;
    }

    // AppStore 검증결과 payMenHistory객체에 저장해서 리턴
    public static PaymentHistory updateWithAppleResponse(PaymentHistory paymentHistory,
                                                         AppleValidationDTO appleValidationDTO) {
        paymentHistory.setOriginalTransactionId(appleValidationDTO.getOriginalTransactionId());
        paymentHistory.setPurchaseDate(parseDateTime(appleValidationDTO.getPurchaseDate()));
        paymentHistory.setBundleId(appleValidationDTO.getBundleId());
        paymentHistory.setEnvironment(appleValidationDTO.getEnvironment());
        paymentHistory.setReceiptValidationResult(appleValidationDTO.getFullResponse());
        paymentHistory.setStatus(PaymentStatus.SUCCESS);
        return paymentHistory;
    }


    //검증 실패 시 PaymentHistory 업데이트
    public static void updateWithValidationFailure(PaymentHistory paymentHistory, String reason) {
        paymentHistory.setStatus(PaymentStatus.VALIDATION_FAILED);
        paymentHistory.setReceiptValidationResult(reason);
    }

    // 날짜 문자열을 LocalDateTime으로 변환
    public static LocalDateTime parseDateTime(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.isEmpty()) {
            return LocalDateTime.now();
        }

        try {
            // ISO 형식으로 파싱 시도
            return LocalDateTime.parse(dateTimeStr);
        } catch (Exception e) {
            // 파싱 실패 시 현재 시간 반환
            return LocalDateTime.now();
        }
    }


    //LocalDateTime을 String으로 변환
    private static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(FORMATTER);
    }



}
