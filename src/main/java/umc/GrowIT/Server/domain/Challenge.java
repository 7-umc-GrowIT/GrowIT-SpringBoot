package umc.GrowIT.Server.domain;

import umc.GrowIT.Server.domain.common.BaseEntity;
import umc.GrowIT.Server.domain.enums.ChallengeType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import umc.GrowIT.Server.domain.enums.ItemCategory;

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

    @Column(length = 255, nullable = false)
    private String content;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer time;

    @Enumerated(EnumType.STRING)
    private ChallengeType dtype;

    @Column(nullable = false)
    private boolean completed;

    @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserChallenge> userChallenges;

    @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChallengeKeyword> challengeKeywords;

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

    // 챌린지 완료 상태로 변경
    public void markAsCompleted() {
        if (this.completed) {
            throw new IllegalStateException("이미 완료된 챌린지입니다.");
        }
        this.completed = true;
    }

    public String getStatus() {
        if (this.dtype != null) {
            return this.dtype.name(); // dtype 값 반환 (RANDOM, DAILY)
        }
        if (this.completed) {
            return "COMPLETED"; // completed가 true인 경우
        }
        return "TOTAL"; // completed가 false인 경우
    }
}