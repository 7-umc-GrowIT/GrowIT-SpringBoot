package umc.GrowIT.Server.domain;

import jakarta.persistence.*;
import lombok.*;
import umc.GrowIT.Server.domain.common.BaseEntity;
import umc.GrowIT.Server.domain.enums.CreditSource;
import umc.GrowIT.Server.domain.enums.DiaryType;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreditHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 크레딧 +- 기록
    @Column(nullable = false)
    private Integer amount;

    // 크레딧 +- 설명
    @Column(nullable = false, length = 100)
    private String description;

    // 크레딧 +- 출처
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CreditSource source;

    // CreditSource별로 관련된 도메인 추적 용도 (FK X)
    @Column(nullable = true)
    private Long referenceId;

    // 과거의 DIARY, CHALLENGE를 관리하기 위한 날짜
    @Column(nullable = true)
    private LocalDate date;

    // CreditSource가 Diary일 경우만 사용되는 일기 타입 (음성 or 텍스트)
    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private DiaryType diaryType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
