package umc.GrowIT.Server.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import umc.GrowIT.Server.domain.common.BaseEntity;
import umc.GrowIT.Server.domain.enums.PaymentStatus;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 기본 정보
    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String productId;

    @Column(nullable = false)
    private Integer creditAmount;

    @Column(nullable = false)
    private Integer paymentAmount;

    @Column(nullable = false)
    private String platform;

    @Column(nullable = false, unique = true)
    private String transactionId;   // 현재거래 식별 id

    // App Store 영수증 원본 데이터
    @Column(columnDefinition = "TEXT")
    private String receiptData;

    // App Store 검증 응답 정보
    private String originalTransactionId; // App Store 원본 거래 ID

    private LocalDateTime purchaseDate; // App Store에서 제공하는 구매 시간

    private String appItemId; // App Store 앱 아이템 ID

    private String bundleId; // 앱 번들 ID

    private Integer amount; // 구매 수량
    
    private String webOrderLineItemId; // 웹 주문 라인 아이템 ID

    private String environment; // Sandbox : 테스트(개발)환경 Production : 실제 결제

    // App Store 검증 결과
    @Column(columnDefinition = "TEXT")
    private String receiptValidationResult; // 전체 검증 응답 JSON

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

}
