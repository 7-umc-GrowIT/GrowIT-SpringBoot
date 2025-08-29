package umc.GrowIT.Server.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.GrowIT.Server.domain.common.BaseEntity;
import umc.GrowIT.Server.domain.enums.TermStatus;
import umc.GrowIT.Server.domain.enums.TermType;

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

    // 약관 적용 상태
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TermStatus status;

    // 약관 효력 발생일
    @Column(nullable = false)
    private LocalDateTime effectiveDate;

    // 약관 종료일
    @Column(nullable = true) 
    private LocalDateTime expirationDate;

    // 버전
    @Column(nullable = false)
    private int version;

    @OneToMany(mappedBy = "term", cascade = CascadeType.ALL)
    private List<UserTerm> userTerm = new ArrayList<>();
}
