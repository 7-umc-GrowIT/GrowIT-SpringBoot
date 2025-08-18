package umc.GrowIT.Server.domain;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import umc.GrowIT.Server.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
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

    @Column(length = 100)
    private String thoughts;

    @Column(columnDefinition = "TEXT")
    private String certificationImageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserChallengeType dtype;

    @Column(nullable = false)
    private boolean completed;

    @Column(nullable = false)
    private LocalDateTime certificationDate;

    @Column(nullable = false)
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
//    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id", nullable = false)
    private Challenge challenge;



    // 인증 작성 (최초 등록 또는 전체 업데이트용)
    public void verifyUserChallenge(ChallengeRequestDTO.ProofRequestDTO proofRequest, String imageUrl){
        this.thoughts = proofRequest.getThoughts();
        this.certificationImageUrl = imageUrl;
        this.certificationDate = LocalDateTime.now(); // 인증한 날짜 저장
        this.completed = true;
    }
}
