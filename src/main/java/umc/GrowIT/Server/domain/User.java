package umc.GrowIT.Server.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import umc.GrowIT.Server.domain.common.BaseEntity;
import umc.GrowIT.Server.domain.enums.Role;
import umc.GrowIT.Server.domain.enums.UserStatus;

import java.util.ArrayList;
import java.util.List;

import static umc.GrowIT.Server.domain.enums.UserStatus.ACTIVE;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 50)
    private String primaryEmail;

    @Column(nullable = false, length = 30)
    private String password;

    @Column(nullable = false, length = 20)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer currentCredit;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer totalCredit;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserChallenge> userChallenges = new ArrayList<>();

    @Setter
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserTerm> userTerms = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Diary> diaries = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<OAuthAccount> oAuthAccounts = new ArrayList<>();

    @Setter
    @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "refresh_token_id", nullable = false)
    private RefreshToken refreshToken;

//    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
//    private RefreshToken refreshToken;



    public void encodePassword(String password) {
        this.password = password;
    }

    public void deleteAccount() {
        if (this.status == ACTIVE) {
            this.status = UserStatus.INACTIVE;
        }
    }

    public void updateCurrentCredit(Integer currentCredit) {
        this.currentCredit = currentCredit;
    }
    public void updateTotalCredit(Integer totalCredit){
        this.totalCredit = totalCredit;
    }

    public void deleteRefreshToken() {
        this.refreshToken = null;
    }

    public void linkUserWithKakaoAccount(String email, String password) {
        this.primaryEmail = email;
        this.password = password;
    }

}