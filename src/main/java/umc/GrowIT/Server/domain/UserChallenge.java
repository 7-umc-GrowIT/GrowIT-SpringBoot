package umc.GrowIT.Server.domain;

import umc.GrowIT.Server.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import umc.GrowIT.Server.domain.enums.ChallengeType;
import umc.GrowIT.Server.web.dto.ChallengeDTO.ChallengeRequestDTO;
import umc.GrowIT.Server.web.dto.ChallengeDTO.ChallengeResponseDTO;

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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChallengeType dtype;

    @Column(name = "completed", nullable = false)
    private boolean completed = false;

    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
    }

    public void verifyUserChallenge(ChallengeRequestDTO.ProofRequestDTO proofRequest){
        thoughts = proofRequest.getThoughts();
        certificationImage = proofRequest.getCertificationImage();
        completed = true;
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
