package umc.GrowIT.Server.domain;

import umc.GrowIT.Server.domain.common.BaseEntity;
import umc.GrowIT.Server.domain.enums.ChallengeStatus;
import jakarta.persistence.*;
import lombok.*;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ChallengeStatus challengeStatus;

    @Column(name = "thoughts", length = 100)
    private String thoughts;

    @Column(name = "certification_image", length = 255)
    private String certificationImage;

    @Column(name = "completed", nullable = false)
    private boolean completed;

    // Challenge 설정 메서드
    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
    }

    // 상태 업데이트
    public void updateStatus(ChallengeStatus newStatus) {
        this.challengeStatus = newStatus;
    }

    // 인증 이미지 수정 메서드
    public void setCertificationImage(String certificationImage) {
        this.certificationImage = certificationImage;
    }

    // 인증 소감 수정 메서드
    public void setThoughts(String thoughts) {
        this.thoughts = thoughts;
    }

}
