package umc.GrowIT.Server.domain;

import jakarta.persistence.*;
import lombok.*;
import umc.GrowIT.Server.domain.common.BaseEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Keyword extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String name;

    @OneToMany(mappedBy = "keyword", cascade = CascadeType.ALL)
    private List<DiaryKeyword> diaryKeywords = new ArrayList<>();

    @OneToMany(mappedBy = "keyword", cascade = CascadeType.ALL)
    private List<ChallengeKeyword> challengeKeywords = new ArrayList<>();
}
