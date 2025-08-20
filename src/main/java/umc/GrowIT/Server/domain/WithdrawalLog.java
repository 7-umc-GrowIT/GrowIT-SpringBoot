package umc.GrowIT.Server.domain;

import jakarta.persistence.*;
import lombok.*;
import umc.GrowIT.Server.domain.common.BaseEntity;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WithdrawalLog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 가입일
    @Column(nullable = false)
    private LocalDateTime joinedAt;

    // 탈퇴일
    @Column(nullable = false)
    private LocalDateTime withdrawalAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "withdrawal_reason_id", nullable = false)
    private WithdrawalReason withdrawalReason;
}