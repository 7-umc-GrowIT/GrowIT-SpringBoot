package umc.GrowIT.Server.domain;

import umc.GrowIT.Server.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import umc.GrowIT.Server.domain.enums.ChallengeType;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserChallenge extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id", nullable = false)
    private Challenge challenge;

    @Column(name = "thoughts", length = 100)
    private String thoughts;

    @Column(name = "certification_image", length = 255)
    private String certificationImage;

    @Column(name = "completed", nullable = false)
    private boolean completed;

    @Enumerated(EnumType.STRING)
    private ChallengeType dtype;

    // Challenge 설정 메서드
    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
    }

    // 인증 완료 상태 설정 메서드
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    // 챌린지 완료 상태로 변경
    public void markAsCompleted() {
        this.completed = true;
    }

    public String getStatus() {
        if (this.dtype != null) {
            return this.completed ? this.dtype.name() + "_COMPLETED" : this.dtype.name(); // 완료하였으면 뒤에 _COMPLETED 추가하여 반환
        }
        return this.completed ? "COMPLETED" : "TOTAL";
    }

}
