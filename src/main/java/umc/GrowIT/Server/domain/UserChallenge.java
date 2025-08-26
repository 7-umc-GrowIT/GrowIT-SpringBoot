package umc.GrowIT.Server.domain;

import jakarta.persistence.*;
import lombok.*;
import umc.GrowIT.Server.domain.common.BaseEntity;
import umc.GrowIT.Server.domain.enums.UserChallengeType;
import umc.GrowIT.Server.web.dto.ChallengeDTO.ChallengeRequestDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserChallenge extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 소감
    @Column(length = 100)
    private String thoughts;

    // 인증사진
    @Column(columnDefinition = "TEXT")
    private String certificationImage;

    // 챌린지 타입 (랜덤 or 데일리)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserChallengeType dtype;

    // 완료여부
    @Column(nullable = false)
    private boolean completed;

    // 인증일시
    @Column(nullable = false)
    private LocalDateTime certificationDate;

    // 날짜(오늘 날짜로 작성한 일기 관련하여 조회하는 용도로 사용)
    @Column(nullable = false)
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id", nullable = false)
    private Challenge challenge;



    // 인증 작성 (최초 등록 또는 전체 업데이트용)
    public void verifyUserChallenge(ChallengeRequestDTO.ProofRequestDTO proofRequest, String imageUrl){
        this.thoughts = proofRequest.getThoughts();
        this.certificationImage = imageUrl;
        this.certificationDate = LocalDateTime.now(); // 인증한 날짜 저장
        this.completed = true;
    }
}
