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

    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
    }

    public void setThoughts(String thoughts) {
        this.thoughts = thoughts;
    }

    public void setCertificationImage(String certificationImage) {
        this.certificationImage = certificationImage;
    }

    // 인증 완료 상태 설정 메서드
    public void updateCompletedStatus(boolean completed) {
        this.completed = completed;
    }

    public String getStatus() {
        return this.completed ? "COMPLETED" : "TOTAL";
    }

}
