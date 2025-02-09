package umc.GrowIT.Server.domain;

import umc.GrowIT.Server.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import umc.GrowIT.Server.domain.enums.UserChallengeType;
import umc.GrowIT.Server.web.dto.ChallengeDTO.ChallengeRequestDTO;

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

<<<<<<< HEAD
    @Column(name = "certification_image", length = 2000)
=======
    @Column(name = "certification_image", columnDefinition = "TEXT")
>>>>>>> b62f6431a43d92ae7196ca726ae795fc09e7c97c
    private String certificationImage;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserChallengeType dtype;

    @Column(nullable = false)
    private boolean completed;

    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
    }

    public void setThoughts(String thoughts) { this.thoughts = thoughts; }

    public void setCertificationImage(String certificationImage) { this.certificationImage = certificationImage; }
    // 인증 작성 (최초 등록 또는 전체 업데이트용)
    public void verifyUserChallenge(ChallengeRequestDTO.ProofRequestDTO proofRequest, String imageUrl){
        thoughts = proofRequest.getThoughts();
        certificationImage = imageUrl;
        completed = true;
    }

}
