package umc.GrowIT.Server.domain;

import jakarta.persistence.*;
import lombok.*;
import umc.GrowIT.Server.domain.common.BaseEntity;
import umc.GrowIT.Server.domain.enums.DiaryStatus;
import umc.GrowIT.Server.domain.enums.DiaryType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Diary extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 일기 날짜 (작성날짜 X)
    @Column(nullable = false)
    private LocalDate date;

    // 일기 내용
    @Lob // 필드를 TEXT로 매핑
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    // 일기 타입 (음성 or 텍스트)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DiaryType type;

    // 일기 상태 (완료 or 대기)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DiaryStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "diary", cascade = CascadeType.ALL)
    private List<DiaryKeyword> diaryKeywords = new ArrayList<>();

    public void setStatus(DiaryStatus status) { this.status = status; }

    public void updateContent(String content) {
        this.content = content;
    }
}
