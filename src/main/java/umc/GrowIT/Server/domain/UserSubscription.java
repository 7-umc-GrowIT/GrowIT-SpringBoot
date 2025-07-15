package umc.GrowIT.Server.domain;

import jakarta.persistence.*;
import lombok.*;
import umc.GrowIT.Server.domain.common.BaseEntity;
import umc.GrowIT.Server.domain.enums.SubscriptionStatus;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserSubscription extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_product_id", nullable = false)
    private SubscriptionProduct subscriptionProduct;

    @Column(name = "transaction_id", nullable = false)
    private String transactionId;

    @Column(name = "original_transaction_id", nullable = false)
    private String originalTransactionId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubscriptionStatus status;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;
}
