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
public class RefreshToken extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // RT값
    @Column(nullable = false, length = 512)
    private String refreshToken;

    // 만료일
    @Column(nullable = false)
    private LocalDateTime expiryDate;

    @OneToOne(mappedBy = "refreshToken")
    private User user;

//    @OneToOne
//    @JoinColumn(name = "user_id", nullable = false)
//    private User user;
}
