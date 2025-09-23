package umc.GrowIT.Server.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import umc.GrowIT.Server.domain.common.BaseEntity;
import umc.GrowIT.Server.domain.enums.Role;
import umc.GrowIT.Server.domain.enums.UserStatus;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 메인 이메일
    @Column(unique = true, length = 50)
    private String primaryEmail;

    // 비밀번호
    @Column(length = 100)
    private String password;

    // 사용자 이름
    @Column(nullable = false, length = 20)
    private String name;

    // 사용자 활성화 여부
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;

    // 사용자 현재 크레딧
    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer currentCredit;

    // 사용자 총 크레딧
    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer totalCredit;

    // 사용자 권한
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

//    // 사용자 구독여부
//    @Column(name = "is_subscribed", nullable = false)
//    private Boolean isSubscribed = false;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserChallenge> userChallenges = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserTerm> userTerms = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Diary> diaries = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<OAuthAccount> oAuthAccounts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<CreditHistory> creditHistories = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "refresh_token_id")
    private RefreshToken refreshToken;

    public void encodePassword(String password) {
        this.password = password;
    }

    public void updateCurrentCredit(Integer currentCredit) {
        this.currentCredit = currentCredit;
    }

    public void updateTotalCredit(Integer totalCredit) {
        this.totalCredit = totalCredit;
    }

    public void deleteRefreshToken() {
        this.refreshToken = null;
    }

    public void linkUserWithKakaoAccount(String email, String password) {
        this.primaryEmail = email;
        this.password = password;
    }

    public void setUserTerms(List<UserTerm> userTerms) {
        this.userTerms = userTerms;
    }

    public void setRefreshToken(RefreshToken refreshTokenEntity) {
        this.refreshToken = refreshTokenEntity;
    }
}
