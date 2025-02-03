package umc.GrowIT.Server.domain;

import umc.GrowIT.Server.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Challenge extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer time;

    @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL)
    private List<UserChallenge> userChallenges;

    @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL)
    private List<ChallengeKeyword> challengeKeywords;

    public boolean isCompletedByUser(Long userId) {
        // 연관된 UserChallenge 중에서 특정 유저가 완료한 상태를 확인
        return userChallenges.stream()
                .anyMatch(userChallenge -> userChallenge.getUser().getId().equals(userId) && userChallenge.isCompleted());
    }

    // 유저-챌린지 추가
    public void addUserChallenge(UserChallenge userChallenge) {
        this.userChallenges.add(userChallenge);
        userChallenge.setChallenge(this);
    }

    // 챌린지-키워드 추가
    public void addChallengeKeyword(ChallengeKeyword challengeKeyword) {
        this.challengeKeywords.add(challengeKeyword);
        challengeKeyword.setChallenge(this);
    }

}