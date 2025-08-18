package umc.GrowIT.Server.domain;

import jakarta.persistence.*;
import lombok.*;
import umc.GrowIT.Server.domain.common.BaseEntity;
import umc.GrowIT.Server.domain.enums.TermType;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Term extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 약관 제목
    @Column(nullable = false, length = 50)
    private String title;

    // 약관 내용
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    // 약관 타입 (필수 or 선택)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TermType type;

    @OneToMany(mappedBy = "term", cascade = CascadeType.ALL)
    private List<UserTerm> userTerm = new ArrayList<>();
}
