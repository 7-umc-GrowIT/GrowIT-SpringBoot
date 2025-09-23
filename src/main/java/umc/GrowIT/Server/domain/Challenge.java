package umc.GrowIT.Server.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import umc.GrowIT.Server.domain.common.BaseEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Challenge extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 제목
    @Column(nullable = false, length = 50)
    private String title;

    // 내용
    @Column(nullable = false)
    private String content;

    // 소요시간
    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer time;

    // 보상 크레딧
    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer credit;

    @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL)
    private List<UserChallenge> userChallenges = new ArrayList<>();

    @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL)
    private List<ChallengeKeyword> challengeKeywords = new ArrayList<>();
}