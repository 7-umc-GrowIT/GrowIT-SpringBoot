package umc.GrowIT.Server.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import umc.GrowIT.Server.domain.common.BaseEntity;
import umc.GrowIT.Server.domain.enums.Provider;
import umc.GrowIT.Server.domain.enums.Role;
import umc.GrowIT.Server.domain.enums.UserStatus;

import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 50)
    private String email;

    @Column(nullable = false)
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
    private Provider provider;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserChallenge> userChallenges;

    @Setter
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserTerm> userTerms;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Diary> diaries;

    @Setter
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "refresh_token_id", nullable = true)
    private RefreshToken refreshToken;

    public void encodePassword(String password) {
        this.password = password;
    }

    public void deleteAccount() {
        if (this.status == UserStatus.ACTIVE) {
            this.status = UserStatus.INACTIVE;
        }
    }

}