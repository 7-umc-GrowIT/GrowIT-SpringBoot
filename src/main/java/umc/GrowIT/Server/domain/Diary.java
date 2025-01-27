package umc.GrowIT.Server.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import umc.GrowIT.Server.domain.common.BaseEntity;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Diary extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date;

    @Lob // 필드를 TEXT로 매핑
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "diary", cascade = CascadeType.ALL)
    private List<DiaryKeyword> diaryKeywords;
}
