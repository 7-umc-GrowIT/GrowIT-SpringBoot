package umc.GrowIT.Server.domain;

import umc.GrowIT.Server.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import umc.GrowIT.Server.domain.enums.UserChallengeType;
import umc.GrowIT.Server.web.dto.ChallengeDTO.ChallengeRequestDTO;

import java.time.LocalDateTime;

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

    @Column(name = "thoughts", length = 100, nullable = false)
    private String thoughts;

    @Column(name = "certification_image", columnDefinition = "TEXT")
    private String certificationImage;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserChallengeType dtype;

    @Column(nullable = false)
    private boolean completed;

    @Column(nullable = false)
    private LocalDateTime certificationDate = LocalDateTime.now(); // null로 저장되는 문제 방지

    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
    }

    public void setThoughts(String thoughts) { this.thoughts = thoughts; }

    public void setCertificationImage(String certificationImage) { this.certificationImage = certificationImage; }
    // 인증 작성 (최초 등록 또는 전체 업데이트용)
    public void verifyUserChallenge(ChallengeRequestDTO.ProofRequestDTO proofRequest, String imageUrl){
        this.thoughts = proofRequest.getThoughts();
        this.certificationImage = imageUrl;
        this.certificationDate = LocalDateTime.now(); // 인증한 날짜 저장
        this.completed = true;
    }

}
