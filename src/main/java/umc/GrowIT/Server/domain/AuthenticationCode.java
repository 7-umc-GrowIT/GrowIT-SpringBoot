package umc.GrowIT.Server.domain;

import jakarta.persistence.*;
import lombok.*;
import umc.GrowIT.Server.domain.common.BaseEntity;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthenticationCode extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 이메일
    @Column(nullable = false, length = 50)
    private String email;

    // 인증 코드
    @Column(unique = true, nullable = false)
    private String code;

    // 인증 여부
    @Column(nullable = false)
    private Boolean isVerified;

    // 인증 유효기간
    @Column(nullable = false)
    private LocalDateTime expirationDate;

    // 만료 처리
    public void expire() {
        this.expirationDate = LocalDateTime.now();
    }

    // 인증 처리
    public void verify() {
        this.isVerified = true;
        this.expirationDate = LocalDateTime.now();
    }
}